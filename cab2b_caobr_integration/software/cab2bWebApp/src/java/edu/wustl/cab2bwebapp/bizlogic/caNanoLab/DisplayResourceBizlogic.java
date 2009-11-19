package edu.wustl.cab2bwebapp.bizlogic.caNanoLab;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.axis.types.URI.MalformedURIException;
import org.cagrid.caObr.client.CaObrClient;

import per.edu.wustl.Annotation;
import per.edu.wustl.Resource;
import edu.wustl.cab2bwebapp.constants.Constants;
import edu.wustl.cab2bwebapp.dvo.AnnotationDVO;
import edu.wustl.cab2bwebapp.dvo.AnnotationElementDVO;

public class DisplayResourceBizlogic {

    public static List<AnnotationDVO> getResult(Annotation[] annotations, HttpSession session) {

        List<AnnotationDVO> dvoList = new ArrayList<AnnotationDVO>();
        Map<String, List<Annotation>> resourceAnnotationMap = new HashMap<String, List<Annotation>>();
        Map<String, Resource> resourceMap = new HashMap<String, Resource>();
        Map<String, AnnotationDVO> resourceAnnotationDVOMap = new HashMap<String, AnnotationDVO>();

        CaObrClient client;
        Resource[] resources = null;
        try {
            client = new CaObrClient("http://ps4266:8080/wsrf/services/cagrid/CaObr");
            resources = client.getAllResources();

        } catch (MalformedURIException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        for (Resource resource : resources) {
            resourceMap.put(resource.getResourceId(), resource);
        }

        for (Annotation annotation : annotations) {
            Resource resource = annotation.getResource().getResource();

            if (resourceAnnotationMap.get(resource.getResourceId()) == null) {
                resourceAnnotationMap.put(resource.getResourceId(), new ArrayList<Annotation>());
            }
            resourceAnnotationMap.get(resource.getResourceId()).add(annotation);
        }

        for (String key : resourceAnnotationMap.keySet()) {
            List<Annotation> anno = resourceAnnotationMap.get(key);
            Resource resource = resourceMap.get(key);

            AnnotationDVO annDvo = new AnnotationDVO();
            annDvo.setResourceLogoURl(resource.getLogoURL());
            annDvo.setResourceName(resource.getName());
            annDvo.setResourceDescription(resource.getDescription());
            annDvo.setResourceId(resource.getResourceId());

            for (Annotation annotation : anno) {
                AnnotationElementDVO eleDVO = new AnnotationElementDVO();
                eleDVO.setElementId(annotation.getElementId());
                eleDVO.setResourceURL(annotation.getUrl());
                eleDVO.setFullDescription(annotation.getDescription());

                String fullDescription = annotation.getDescription();
                int length = fullDescription.length();

                if (length < 145) {
                    eleDVO.setDescription(fullDescription);
                } else {
                    eleDVO.setDescription(fullDescription.substring(0, 145));
                }
                annDvo.setList(eleDVO);
            }
            resourceAnnotationDVOMap.put(annDvo.getResourceId(), annDvo);
            dvoList.add(annDvo);
        }

        session.setAttribute(Constants.RESOURCE_ANNOTATIONMAPS, resourceAnnotationDVOMap);

        for (String resourceId : resourceMap.keySet()) {

            if (!resourceAnnotationMap.containsKey(resourceId)) {
                Resource resource = resourceMap.get(resourceId);
                AnnotationDVO annDvo = new AnnotationDVO();
                annDvo.setResourceLogoURl(resource.getLogoURL());
                annDvo.setResourceName(resource.getName());
                annDvo.setResourceDescription(resource.getDescription());
                dvoList.add(annDvo);
            }
        }

        return dvoList;
    }

}
