package pos.machine;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PosMachine {

    private final Map<String, Item> itemMap;

    public PosMachine() {
        this.itemMap = decodeToItems(ItemsLoader.loadAllItems());
    }

    private Map<String, Item> decodeToItems(List<Item> items) {
        return items.stream()
                .collect(Collectors.toMap(Item::getBarcode, item -> item));
    }
    public String printReceipt(List<String> barcodes) {
        return null;
    }
}
