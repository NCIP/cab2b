package edu.wustl.cab2bwebapp.bizlogic.caNanoLab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import obr.ClientUtility;
import per.edu.wustl.Resource;
import edu.wustl.cab2bwebapp.constants.Constants;
import edu.wustl.cab2bwebapp.dvo.SearchResultDVO;

public class DisplayResourceBizlogic {

    private static Map<String, List<String>> parseResult(String[] resourceInfo) {

        Map<String, List<String>> resourceInfoMap = new HashMap<String, List<String>>();

        for (String s : resourceInfo) {
            String[] arr = s.split("custom_separator");

            String resourceId = arr[0];
            String resourceDescription = arr[1];
            String resourceLogo = arr[2];
            String resourceName = arr[3];
            String elementId = arr[4];
            String elementDescription = arr[5];
            String resourceUrl = arr[6];

            String key = resourceId + "custom_separator" + resourceDescription + "custom_separator" + resourceLogo
                    + "custom_separator" + resourceName;
            String value = elementId + "custom_separator" + elementDescription + "custom_separator" + resourceUrl;

            List<String> values = resourceInfoMap.get(key);

            if (values == null) {
                values = new ArrayList<String>();
            }
            values.add(value);
            resourceInfoMap.put(key, values);
        }

        return resourceInfoMap;
    }

    public static List<List<SearchResultDVO>> getResult(String[] resourceInfo, HttpSession session) {
        Map<String, List<String>> parsedResult = parseResult(resourceInfo);
        Set<String> resourceIdSet = new HashSet<String>();
        List<List<SearchResultDVO>> listSearch = new ArrayList<List<SearchResultDVO>>();
        session.setAttribute(Constants.PARSED_RESULT, parsedResult);

        for (String key : parsedResult.keySet()) {

            String keyValues[] = key.split("custom_separator");
            String resourceId = keyValues[0];
            String resourceDescription = keyValues[1];
            String resourceLogo = keyValues[2];
            String resourceName = keyValues[3];
            resourceIdSet.add(resourceId + resourceName);

            List<SearchResultDVO> listDVO = new ArrayList<SearchResultDVO>();

            SearchResultDVO dvo1 = new SearchResultDVO();
            dvo1.setTitle("Resource");
            dvo1.setValue("<img src='" + resourceLogo + "' title='" + resourceDescription + "'" + "/> <br>"
                    + "<span style=font-weight:bold>" + resourceName + "</span>");

            SearchResultDVO dvo2 = new SearchResultDVO();
            dvo2.setTitle("Annotation Found");
            StringBuilder temp = new StringBuilder();
            int countToken = 0;
            List<String> elementIds = parsedResult.get(key);
            int noOfAnnotation = elementIds.size();
            for (String val : elementIds) {
                String values[] = val.split("custom_separator");
                String elementId = values[0];
                String elementDesc = values[1];
                String resourceUrl = values[2];
                if (countToken < 4) {
                    int length = elementDesc.length();
                    String desc = "";
                    if (length < 148) {
                        desc = elementDesc + "....<br>";
                    } else
                        desc = elementDesc.substring(0, 147) + "....<br>";

                    temp = temp.append(
                                       "<a class='link' href='" + resourceUrl + "' target='_blank'" + "title='"
                                               + elementDesc + "'>" + elementId + "</a>").append(
                                                                                                 ": &nbsp;&nbsp;&nbsp;&nbsp;").append(
                                                                                                                                      desc);
                }

                if (countToken == 4) {
                    String s = "<a class='link' href='javascript:void(0)' onClick='showWindow(\"" + key
                            + "\")'><span style=\"float:right;margin-right: 2em;\">View All( Annotation:"
                            + noOfAnnotation + ")</span></a>";

                    temp = temp.append(s);
                    break;
                }
                countToken++;
            }
            dvo2.setValue(temp.toString());
            listDVO.add(dvo1);
            listDVO.add(dvo2);
            listSearch.add(listDVO);
        }

        for (Resource resource : ClientUtility.getResources()) {

            if (!resourceIdSet.contains(resource.getResourceId() + resource.getResourceName())) {
                List<SearchResultDVO> listDVO = new ArrayList<SearchResultDVO>();
                SearchResultDVO dvo1 = new SearchResultDVO();
                dvo1.setTitle("Resource");
                dvo1.setValue("<img src='" + resource.getResourceLogo() + "' title='"
                        + resource.getResourceDescription() + "'" + "/> <br>" + "<span style=font-weight:bold>"
                        + resource.getResourceName() + "</span>");

                /*dvo1.setValue("<img src='" + resource.getResourceLogo() + "' title='"
                        + resource.getResourceDescription() + "'" + "/>");
                */
                SearchResultDVO dvo2 = new SearchResultDVO();
                dvo2.setTitle("Annotation Found");
                dvo2.setValue("<span style='color: blue;'>No Annotation Found </span>");
                listDVO.add(dvo1);
                listDVO.add(dvo2);
                listSearch.add(listDVO);
            }

        }
        return listSearch;
    }

    
  private static  boolean isDuplicated(String pattern, String value) {
        
        String newPatter = "\\s+"+pattern.trim() + "\\s+";
        Pattern p = Pattern.compile(newPatter, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(value);
        return  m.find();

  }
    public static void main(String[] args) {
        
        String s1 = " Complementary DNA sequence 3'SH A10 ATT GTT ATT 5' (AT-rich sequence) ";
        String s2 = " Complementary DNA sequence 3'SH A10 ATT GTT ATT 5' (AT-rich sequence) "; 
        System.out.println(isDuplicated(s1,s2));
        

    }
}
