import java.util.List;


/**
 *
 * @author Manusporn Fukkham
 */
public class XMLManager {
    public String node;
    public List<Dictionary> attribute;
    public List<XMLManager> child;

    public XMLManager(String node, List<Dictionary> attribute, List<XMLManager> child) {
        this(node, child);
        this.attribute = attribute;
    }

    public XMLManager(String node, List<XMLManager> child) {
        this(node);
        this.child = child;
    }

    public XMLManager(String node) {
        this.node = node;
    }
}
