package edu.wustl.cab2bwebapp.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2bwebapp.bizlogic.UserBackgroundQueries;
import edu.wustl.cab2bwebapp.bizlogic.executequery.QueryBizLogic;
import edu.wustl.cab2bwebapp.constants.Constants;

/**
 * @author pallavi_mistry
 *
 */
public class ExportResultsAction extends Action {
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(ExportResultsAction.class);

    /* (non-Javadoc)
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) {
        try {
            if (request.getParameter("fileName") == null) {
                String filePath = UserBackgroundQueries.EXPORT_CSV_DIR + File.separator;
                HttpSession session = request.getSession();
                QueryBizLogic queryBizLogic =
                        (QueryBizLogic) session.getAttribute(Constants.QUERY_BIZ_LOGIC_OBJECT);
                String exported_file_path = (String) session.getAttribute(Constants.EXPORTED_FILE_PATH);
                //File is getting exported (and thus built) first time.
                if (exported_file_path == null && !((" ").equals(exported_file_path))) {
                    String newfilename = queryBizLogic.exportToCSV();
                    exported_file_path = filePath + newfilename;
                    session.setAttribute(Constants.EXPORTED_FILE_PATH, exported_file_path);
                }
                //else take the file path from the session
                sendFileToClient(response, exported_file_path, "Results.zip", "application/download");
            } else {
                sendFileToClient(response, UserBackgroundQueries.EXPORT_CSV_DIR + File.separator
                        + request.getParameter("fileName"), "Results.zip", "application/download");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ActionErrors errors = new ActionErrors();
            ActionError error = new ActionError("fatal.exportresults.failure", e.getMessage());
            errors.add(Constants.FATAL_EXPORT_FAILURE, error);
            saveErrors(request, errors);
            return mapping.findForward(Constants.FORWARD_FAILURE);
        }

        return null;
    }

    /**
     * @param response
     * @param filePath
     * @param fileName
     * @param contentType
     */
    public static void sendFileToClient(HttpServletResponse response, String filePath, String fileName,
                                        String contentType) {
        if (filePath != null && (filePath.length() != 0)) {
            File file = new File(filePath);
            if (file.exists()) {
                response.setContentType(contentType);
                response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\";");
                response.setContentLength((int) file.length());
                writeToStream(response, file);
            } else {
                logger.error("Exported result file does not exist.");
                throw new RuntimeException("Exported result file does not exist.");
            }
        }
    }

    /**
     * @param response
     * @param file
     */
    private static void writeToStream(HttpServletResponse response, File file) {
        BufferedInputStream bis = null;
        try {
            response.setHeader("Pragma", "public");
            response.setHeader("Cache-Control", "max-age=0");

            OutputStream opstream = response.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(file));

            byte[] buf = new byte[Constants.FOUR_KILO_BYTES];
            int count = bis.read(buf);
            while (count > -1) {
                opstream.write(buf, 0, count);
                count = bis.read(buf);
            }
            opstream.flush();
            bis.close();
            opstream.close();
        } catch (FileNotFoundException ex) {
            logger.error("Exception in method sendFileToClient:" + ex.getMessage(), ex);
            throw new RuntimeException("Error while exporting results file.");
        } catch (IOException ex) {
            logger.error("Exception in method sendFileToClient:" + ex.getMessage(), ex);
            throw new RuntimeException("Error while doing file operations.");
        }
    }
}