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

    private Map<String, Integer> calculateItemsCost(List<String> barcodes) {
        return barcodes.stream()
                .collect(Collectors.groupingBy(barcode -> barcode, Collectors.summingInt(b -> 1)));
    }

    private int calculateTotalPrice(Map<String, Integer> itemCounts) {
        return itemCounts.entrySet().stream()
                .mapToInt(entry -> {
                    Item item = itemMap.get(entry.getKey());
                    return (item != null) ? item.getPrice() * entry.getValue() : 0;
                })
                .sum();
    }

    public String printReceipt(List<String> barcodes) {
        return null;
    }
}
