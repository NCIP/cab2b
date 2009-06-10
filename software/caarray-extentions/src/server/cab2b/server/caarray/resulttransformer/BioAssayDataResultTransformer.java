package cab2b.server.caarray.resulttransformer;

import java.util.Set;

import cab2b.common.caarray.BioAssayDataRecord;
import cab2b.common.caarray.IPartiallyInitializedBioAssayDataRecord;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.queryengine.result.RecordId;
import gov.nih.nci.caarray.domain.data.QuantitationType;

/**
 * Transformer that creates {@link IPartiallyInitializedBioAssayDataRecord} for
 * queries on {@link BioAssayData} and its subtypes. This transformer is needed
 * to "understand" the {@link BioDataCube} returned by the caArray service along
 * with a {@link BioAssayData}.
 * <p>
 * Currently, the biodatacube is always transformed to BQD. The names of the
 * three dimensions are fetched by firing additional CQLs.
 * 
 * @author srinath_k
 * 
 */
public class BioAssayDataResultTransformer
        extends
        AbstractCaArrayResultTransfomer<IPartiallyInitializedBioAssayDataRecord> {
    private static final String HEADER_ATTRIBUTE_NAME = "name";

    /**
     * Creates a {@link BioAssayDataRecord} of type "fully initialized".
     * 
     * @see cab2b.server.caarray.resulttransformer.AbstractCaArrayResultTransfomer#createCaArrayRecord(java.util.Set,
     *      edu.wustl.cab2b.common.queryengine.result.RecordId)
     * @see BioAssayDataRecord#createFullyInitializedRecord(Set, RecordId)
     */
    @Override
    protected IPartiallyInitializedBioAssayDataRecord createCaArrayRecord(Set<AttributeInterface> attributes,
                                                                          RecordId id) {
        return BioAssayDataRecord.createFullyInitializedRecord(attributes, id);
    }

    /**
     * Creates the {@link IPartiallyInitializedBioAssayDataRecord} for given
     * object.<br>
     * Following is the sequence of steps:
     * <ol>
     * <li>Call <code>super.createRecordForObject()</code>. This creates a
     * new record that contains the values of the attributes and associated ids.</li>
     * <li>Obtain the names of the dimensions i.e. names of {@link BioAssay},
     * {@link QuantitationType} and {@link DesignElement} by firing appropriate
     * CQLs. These names are the names of the three dimensions of the
     * {@link BioDataCube}. Populate these names as the dim1Labels, dim2Labels
     * and dim3Labels respectively.</li>
     * <li>Transform the three dimensional array of {@link BioDataCube} to the
     * order BQD using {@link MatrixDimensionSwapper}.</li>
     * <li>Return the lazy form of this record obtained by
     * {@link BioAssayDataRecord#createLazyForm(BioAssayDataRecord)}.</li>
     * </ol>
     * 
     * @throws IllegalArgumentException if the given object is not an
     *             <code>instanceOf</code> {@link BioAssayData}.
     * @see cab2b.server.caarray.resulttransformer.AbstractCaArrayResultTransfomer#createRecordForObject(java.lang.String,
     *      java.lang.Object,
     *      edu.common.dynamicextensions.domaininterface.EntityInterface)
     */
    @Override
    protected IPartiallyInitializedBioAssayDataRecord createRecordForObject(String url, Object objRec,
                                                                            EntityInterface outputEntity) {
        /* OLD CODE
         if (!(objRec instanceof BioAssayData)) {
         throw new IllegalArgumentException();
         }
         BioAssayDataRecord rec = (BioAssayDataRecord) super.createRecordForObject(url, objRec, outputEntity);
         BioAssayData derivedBioAssayData = (BioAssayData) objRec;
         String[] bioAssayNames = getBioAssaysNames(derivedBioAssayData.getBioAssayDimension().getIdentifier(), url);
         String[] quantitationTypeNames = getQuantitationTypesNames(
         derivedBioAssayData.getQuantitationTypeDimension().getIdentifier(),
         url);
         String[] designElementNames = getDesignElementsNames(
         derivedBioAssayData.getDesignElementDimension().getIdentifier(),
         url);

         rec.setDim1Labels(bioAssayNames);
         rec.setDim2Labels(quantitationTypeNames);
         rec.setDim3Labels(designElementNames);

         BioDataCube bioDataCube = (BioDataCube) derivedBioAssayData.getBioDataValues();
         rec.setCube(transformCubeToBQD(bioDataCube));

         return BioAssayDataRecord.createLazyForm(rec);

         // return (IPartiallyInitializedBioAssayDataRecord)
         // LazyInitializer.getView(
         // BioAssayDataRecord.createLazyForm(
         // rec).handle(),
         // new LazyParams(
         // Collections.singletonList(new Range(
         // 0,
         // bioAssayNames.length,
         // 0,
         // quantitationTypeNames.length,
         // 0,
         // designElementNames.length))));
         */
        return null;
    }
    /* OLD CODE
     private Object[][][] transformCubeToBQD(BioDataCube bioDataCube) {
     String order = bioDataCube.getOrder();
     int[] dimensionMap = new int[3];
     char[] bqdOrder = { 'B', 'Q', 'D' };
     for (int i = 0; i < 3; i++) {
     dimensionMap[i] = order.indexOf(bqdOrder[i]);
     }
     return (Object[][][]) MatrixDimensionSwapper.swapDimensions(bioDataCube.getCube(), dimensionMap);
     }

     // begin bioassay
     private String[] getBioAssaysNames(String bioAssayDimensionId, String url) {
     BioAssayDimension bioAssayDimension = getBioAssayDimensionById(bioAssayDimensionId, url);
     return queryForNames(url, getIds(bioAssayDimension.getBioAssays()), BioAssay.class.getName());
     }

     private BioAssayDimension getBioAssayDimensionById(String identifier, String url) {
     return transformerUtil.getObjectById(BioAssayDimension.class, identifier, url);
     }

     // end bioassay

     // begin quantitationType
     private String[] getQuantitationTypesNames(String quantitationTypeDimensionId, String url) {
     QuantitationTypeDimension quantitationTypeDimension = getQuantitationTypeDimensionById(
     quantitationTypeDimensionId,
     url);
     return getNames(quantitationTypeDimension.getQuantitationTypes());
     }

     private QuantitationTypeDimension getQuantitationTypeDimensionById(String identifier, String url) {
     return transformerUtil.getObjectById(QuantitationTypeDimension.class, identifier, url);
     }

     // end quantitationType

     // begin designElements
     private String[] getDesignElementsNames(String designElementDimensionId, String url) {
     CompositeSequenceDimension compositeSequenceDimension = getCompositeSequenceDimensionById(
     designElementDimensionId,
     url);

     return getNames(compositeSequenceDimension.getCompositeSequences());
     }

     private CompositeSequenceDimension getCompositeSequenceDimensionById(String identifier, String url) {
     return transformerUtil.getObjectById(CompositeSequenceDimension.class, identifier, url);
     }

     // end designElements

     private String[] queryForNames(String url, String[] ids, String className) {
     Map<String, String> idToHeader = new HashMap<String, String>(ids.length);

     List<Map<String, String>> results = transformerUtil.getAttributeResult(
     className,
     new String[] { IDENTIFIER_ATTRIBUTE_NAME, HEADER_ATTRIBUTE_NAME },
     IDENTIFIER_ATTRIBUTE_NAME, ids, url);
     for (Map<String, String> row : results) {
     idToHeader.put(row.get(IDENTIFIER_ATTRIBUTE_NAME), row.get(HEADER_ATTRIBUTE_NAME));
     }
     String[] headers = new String[ids.length];
     for (int i = 0; i < ids.length; i++) {
     headers[i] = idToHeader.get(ids[i]);
     }
     return headers;
     }

     private String[] getIds(Identifiable[] identifiables) {
     String[] ids = new String[identifiables.length];
     for (int i = 0; i < ids.length; i++) {
     ids[i] = identifiables[i].getIdentifier();
     }
     return ids;
     }

     private String[] getNames(Identifiable[] identifiables) {
     String[] names = new String[identifiables.length];
     for (int i = 0; i < names.length; i++) {
     names[i] = identifiables[i].getName();
     }
     return names;
     }

     // ////////////////// HACKS FOLLOW, BEWARE!! ////////////////////////

     // @Override
     // public IQueryResult<IPartiallyInitializedBioAssayDataRecord>
     // getResults(DCQLQuery query,
     // EntityInterface targetEntity) {
     //
     // return getQueryResult(targetEntity);
     // }

     private IQueryResult<IPartiallyInitializedBioAssayDataRecord> getQueryResult(EntityInterface targetEntity) {
     System.out.println("hack");
     IQueryResult<IPartiallyInitializedBioAssayDataRecord> queryResults = QueryResultFactory.createResult(targetEntity);
     try {
     BioAssayDataRecord rec = (BioAssayDataRecord) LazyInitializer.getFullyInitialializedRecord(0);
     System.out.println("Found cube in cache; returning it...");
     queryResults.addRecord("http://caarraydb-stage.nci.nih.gov/wsrf/services/caGrid/CaArraySvc",
     BioAssayDataRecord.createLazyForm(rec));
     return queryResults;
     } catch (IllegalArgumentException e) {
     System.out.println("Reading cube from file first time...");
     }
     BioAssayDataRecord record = BioAssayDataRecord.createFullyInitializedRecord(new HashSet(
     targetEntity.getAttributeCollection()), new RecordId(
     "gov.nih.nci.ncicb.caarray:DerivedBioAssayData:1015897589771984:1", "asdf"));

     try {

     int cnt = 0;

     int rowNo = 0;

     String dim1Labels[] = null;

     String dim2Labels[] = null;

     String dim3Labels[] = null;

     System.out.println("Creating a 3D array from file");

     BufferedReader fileReader = new BufferedReader(new FileReader(new File("d:\\input.txt")));

     String str = fileReader.readLine();

     StringTokenizer tokenizer = new StringTokenizer(str, "\t");

     cnt = tokenizer.countTokens();

     rowNo = 54676;

     dim1Labels = new String[] { "" };

     dim2Labels = new String[cnt];

     dim3Labels = new String[rowNo];

     // get column names

     tokenizer.nextToken();

     for (int i = 0; i < cnt - 1; i++) {

     dim2Labels[i] = tokenizer.nextToken();

     }

     tokenizer = null;

     Object bioDataCube[][][] = new Object[1][cnt][rowNo];

     for (int i = 0; i < cnt; i++) {

     bioDataCube[0][i] = new Object[rowNo];

     }

     str = fileReader.readLine();

     int k = 0;

     while (str != null) {

     String[] tokens = str.split("\t");

     dim3Labels[k] = tokens[0];

     for (int i = 0; i < cnt - 1; i++) {

     try {

     bioDataCube[0][i][k] = Float.valueOf(tokens[i + 1]).floatValue();

     } catch (NumberFormatException e) {

     bioDataCube[0][i][k] = tokens[i + 1];

     }

     }

     k++;

     str = null;

     tokens = null;

     str = fileReader.readLine();

     if (k % 300 == 0)

     System.out.println("parsed line " + k);

     }

     fileReader.close();

     fileReader = null;

     record.setDim1Labels(dim1Labels);

     record.setDim2Labels(dim2Labels);

     record.setDim3Labels(dim3Labels);

     record.setCube(bioDataCube);

     for (AttributeInterface attribute : targetEntity.getAttributeCollection()) {

     record.putValueForAttribute(attribute, "1");

     }

     } catch (Exception e) {

     e.printStackTrace();

     }

     // queryResults.addRecord(
     // "http://caarraydb-stage.nci.nih.gov/wsrf/services/caGrid/CaArraySvc",
     // (IPartiallyInitializedBioAssayDataRecord) LazyInitializer.getView(
     // BioAssayDataRecord.createLazyForm(
     // record).handle(),
     // new LazyParams(
     // Collections.singletonList(new Range(
     // 0,
     // record.getDim1Labels().length,
     // 0,
     // record.getDim2Labels().length,
     // 0,
     // record.getDim3Labels().length)))));

     queryResults.addRecord("http://caarraydb-stage.nci.nih.gov/wsrf/services/caGrid/CaArraySvc",
     BioAssayDataRecord.createLazyForm(record));

     return queryResults;

     }
     // private IQueryResult<IPartiallyInitializedBioAssayDataRecord>
     // getQueryResult(EntityInterface targetEntity) {
     // IQueryResult<IPartiallyInitializedBioAssayDataRecord> queryResults =
     // QueryResultFactory.createResult(targetEntity);
     //
     // BioAssayDataRecord record =
     // BioAssayDataRecord.createFullyInitializedRecord(new HashSet(
     //
     // targetEntity.getAttributeCollection()), new RecordId(
     //
     // "gov.nih.nci.ncicb.caarray:DerivedBioAssayData:1015897589771984:1",
     // "asdf"));
     //
     // String dim1Labels[] = { "1" };
     //
     // String dim2Labels[] = { "Pairs", "Pairs Used", "Signal", "Detection",
     // "Detection P-value" };
     //
     // String dim3Labels[] = { "92555_at", "92558_at", "92559_at", "92568_at",
     // "92574_at", "92555_at", "92558_at", "92559_at", "92568_at", "92574_at",
     // "92555_at", "92558_at", "92559_at", "92568_at", "92574_at", "92555_at",
     // "92558_at", "92559_at", "92568_at", "92574_at", "92555_at", "92558_at",
     // "92559_at", "92568_at", "92574_at", "92555_at", "92558_at", "92559_at",
     // "92568_at", "92574_at" };
     //
     // Object bioDataCube[][][] = new Object[][][] { { { "20.0", "16.0", "20.0",
     // "16.0", "20.0", "20.0", "16.0", "20.0", "16.0", "20.0", "20.0", "16.0",
     // "20.0", "16.0", "20.0", "20.0", "16.0", "20.0", "16.0", "20.0", "20.0",
     // "16.0", "20.0", "16.0", "20.0", "20.0", "16.0", "20.0", "16.0", "20.0" },
     // { "Absent", "Present", "Present", "Marginal", "Absent", "Absent",
     // "Present", "Present", "Marginal", "Absent", "Absent", "Present",
     // "Present", "Marginal", "Absent", "Absent", "Present", "Present",
     // "Marginal", "Absent", "Absent", "Present", "Present", "Marginal",
     // "Absent", "Absent", "Present", "Present", "Marginal", "Absent" }, {
     // "0.13876513", "0.3276513", "0.5645876513", "0.464376513", "0.235876513",
     // "0.13876513", "0.3276513", "0.5645876513", "0.464376513", "0.235876513",
     // "0.13876513", "0.3276513", "0.5645876513", "0.464376513", "0.235876513",
     // "0.13876513", "0.3276513", "0.5645876513", "0.464376513", "0.235876513",
     // "0.13876513", "0.3276513", "0.5645876513", "0.464376513", "0.235876513",
     // "0.13876513", "0.3276513", "0.5645876513", "0.464376513", "0.235876513"
     // }, { "2.188886E-4", "3.188886E-4", "4.188886E-4", "5.188886E-4",
     // "6.188886E-4", "2.188886E-4", "3.188886E-4", "4.188886E-4",
     // "5.188886E-4", "6.188886E-4", "2.188886E-4", "3.188886E-4",
     // "4.188886E-4", "5.188886E-4", "6.188886E-4", "2.188886E-4",
     // "3.188886E-4", "4.188886E-4", "5.188886E-4", "6.188886E-4",
     // "2.188886E-4", "3.188886E-4", "4.188886E-4", "5.188886E-4",
     // "6.188886E-4", "2.188886E-4", "3.188886E-4", "4.188886E-4",
     // "5.188886E-4", "6.188886E-4" }, { "0.0030666848", "0.0030666848",
     // "0.0030666848", "0.0030666848", "0.0030666848", "0.0030666848",
     // "0.0030666848", "0.0030666848", "0.0030666848", "0.0030666848",
     // "0.0030666848", "0.0030666848", "0.0030666848", "0.0030666848",
     // "0.0030666848", "0.0030666848", "0.0030666848", "0.0030666848",
     // "0.0030666848", "0.0030666848", "0.0030666848", "0.0030666848",
     // "0.0030666848", "0.0030666848", "0.0030666848", "0.0030666848",
     // "0.0030666848", "0.0030666848", "0.0030666848", "0.0030666848" } } };
     //
     // for (AttributeInterface attribute :
     // targetEntity.getAttributeCollection()) {
     //
     // record.putValueForAttribute(attribute, "1");
     //
     // }
     //
     // record.setDim1Labels(dim1Labels);
     //
     // record.setDim2Labels(dim2Labels);
     //
     // record.setDim3Labels(dim3Labels);
     //
     // record.setCube(bioDataCube);
     //
     // queryResults.addRecord("someUrl",
     // BioAssayDataRecord.createLazyForm(record));
     //
     // return queryResults;
     //
     // }
     */
}
