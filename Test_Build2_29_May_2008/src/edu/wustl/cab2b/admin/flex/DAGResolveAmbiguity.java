package edu.wustl.cab2b.admin.flex;


public class DAGResolveAmbiguity {

//    private IPathFinder pathFinder;
//
//    public DAGResolveAmbiguity(IPathFinder pathFinder) {
//
//        this.pathFinder = pathFinder;
//    }
//
//    /**
//     * Method to get all possible paths for given source and destination entity
//     * 
//     * @param sourceEntity
//     * @param destinationEntity
//     */
//    public  Map<String, List<IPath>> getPaths(EntityInterface sourceEntity, EntityInterface destinationEntity) {
//        Set<ICuratedPath> allCuratedPaths = pathFinder.getCuratedPaths(sourceEntity, destinationEntity);
//        Logger.out.debug("  getCuratedPaths() executed : " + allCuratedPaths.size());
//        List<IPath> curratedPathList = new ArrayList<IPath>();
//        ;
//        List<IPath> selectedPathList = new ArrayList<IPath>();
//        ;
//
//        for (ICuratedPath iCuratedPaths : allCuratedPaths) {
//            Set<IPath> iPathSet = iCuratedPaths.getPaths();
//            //called pathfinder for 2 entities , so this set will have only 1 IPath in it
//            if (!iPathSet.isEmpty()) {
//                IPath iPath = iPathSet.iterator().next();
//                if (iCuratedPaths.isSelected()) {
//                    selectedPathList.add(iPath);
//                }
//                curratedPathList.add(iPath);
//            }
//        }
//        List<IPath> generalPathList = pathFinder.getAllPossiblePaths(sourceEntity, destinationEntity);
//        Map<String, List<IPath>> allPathMap = new HashMap<String, List<IPath>>(3);
//        allPathMap.put(Constants.SELECTED_PATH, selectedPathList);
//        allPathMap.put(Constants.CURATED_PATH, curratedPathList);
//        allPathMap.put(Constants.GENERAL_PATH, generalPathList);
//        return allPathMap;
//    }
}