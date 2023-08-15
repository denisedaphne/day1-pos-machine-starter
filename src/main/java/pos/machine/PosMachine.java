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

    private String generateItemsReceipt(Map<String, Integer> itemCounts) {
        return itemCounts.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    String barcode = entry.getKey();
                    int quantity = entry.getValue();
                    Item item = itemMap.get(barcode);
                    if (item != null) {
                        int subtotal = item.getPrice() * quantity;
                        return String.format("Name: %s, Quantity: %d, Unit price: %d (yuan), Subtotal: %d (yuan)\n",
                                item.getName(), quantity, item.getPrice(), subtotal);
                    }
                    return "";
                })
                .collect(Collectors.joining());
    }

    private String generateReceipt(int totalAmount, String itemsReceipt) {
        return "***<store earning no money>Receipt***\n" +
                itemsReceipt +
                "----------------------\n" +
                String.format("Total: %d (yuan)\n", totalAmount) +
                "**********************";
    }

    public String printReceipt(List<String> barcodes) {
        if (barcodes == null || barcodes.isEmpty()) {
            return "";
        }

        Map<String, Integer> itemCounts = calculateItemsCost(barcodes);
        int totalAmount = calculateTotalPrice(itemCounts);
        String itemsReceipt = generateItemsReceipt(itemCounts);
        return generateReceipt(totalAmount, itemsReceipt);
    }
}
