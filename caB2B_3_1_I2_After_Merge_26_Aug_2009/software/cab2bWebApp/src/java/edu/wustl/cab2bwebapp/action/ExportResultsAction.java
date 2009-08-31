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
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.cab2bwebapp.bizlogic.UserBackgroundQueries;
import edu.wustl.cab2bwebapp.bizlogic.executequery.QueryBizLogic;
import edu.wustl.cab2bwebapp.constants.Constants;

public class ExportResultsAction extends Action {
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(ExportResultsAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        if (request.getParameter("fileName") == null) {
            String filePath = UserBackgroundQueries.EXPORT_CSV_DIR + File.separator;
            HttpSession session = request.getSession();
            QueryBizLogic queryBizLogic = (QueryBizLogic) session.getAttribute(Constants.QUERY_BIZ_LOGIC_OBJECT);
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
        return null;
    }

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
                logger.error("Sorry Cannot Download as fileName is null");
            }
        }
    }

    private static void writeToStream(HttpServletResponse response, File file) {
        BufferedInputStream bis = null;
        try {
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
        } catch (IOException ex) {
            logger.error("Exception in method sendFileToClient:" + ex.getMessage(), ex);
        }
    }
}