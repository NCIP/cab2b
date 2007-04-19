package edu.wustl.cab2b.common.queryengine.result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wustl.common.querysuite.metadata.category.Category;

public class CategoryResult implements ICategoryResult {
    /**
     * 
     */
    private static final long serialVersionUID = -1466604031613589142L;

    private Category category;

    private Map<String, List<ICategorialClassRecord>> urlToRootRecords;

    public Category getCategory() {
        return category;
    }

    public Map<String, List<ICategorialClassRecord>> getUrlToRootRecords() {
        if (urlToRootRecords == null) {
            urlToRootRecords = new HashMap<String, List<ICategorialClassRecord>>();
        }
        return urlToRootRecords;
    }

    public boolean isCategoryResult() {
        return true;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setUrlToRootRecords(
                                    Map<String, List<ICategorialClassRecord>> urlToRootRecords) {
        this.urlToRootRecords = urlToRootRecords;
    }

    public void putCategorialClassRecordsForUrl(
                                                String url,
                                                List<? extends ICategorialClassRecord> recs) {
        getUrlToRootRecords().put(url,
                                  new ArrayList<ICategorialClassRecord>(recs));
    }

    public void addCategorialClassRecordsForUrl(
                                                String url,
                                                List<? extends ICategorialClassRecord> recs) {
        List<ICategorialClassRecord> recsList;
        if (getUrlToRootRecords().containsKey(url)) {
            recsList = getUrlToRootRecords().get(url);
        } else {
            recsList = new ArrayList<ICategorialClassRecord>();
            getUrlToRootRecords().put(url, recsList);
        }
        recsList.addAll(recs);
    }

    public void copyRecords(CategoryResult other) {
        for (Map.Entry<String, List<ICategorialClassRecord>> entry : other.getUrlToRootRecords().entrySet()) {
            addCategorialClassRecordsForUrl(entry.getKey(), entry.getValue());
        }
    }
}
