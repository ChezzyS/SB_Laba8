package MyFirstTestAppSpringBoot.Ctrl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
public class MyCtrl {
    private ArrayList<String> list;
    private HashMap<Integer, String> map;

   //1
    @GetMapping("/update-array")
    public String updateArrayList(@RequestParam String s) {
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(s);
        return "Добавлено в ArrayList: " + s;
    }

    //2
    @GetMapping("/show-array")
    public ArrayList<String> showArrayList() {
        if (list == null) {
            return new ArrayList<>();
        }
        return list;
    }

    //3
    @GetMapping("/update-map")
    public String updateHashMap(@RequestParam String s) {
        if (map == null) {
            map = new HashMap<>();
        }
        // ключ - порядковый номер нового элемента
        int key = map.size() + 1;
        map.put(key, s);
        return "Добавлено в HashMap: " + key + " -> " + s;
    }

    //4
    @GetMapping("/show-map")
    public HashMap<Integer, String> showHashMap() {
        if (map == null) {
            return new HashMap<>();
        }
        return map;
    }

    //5
    @GetMapping("/show-all-lenght")
    public String showAllLenght() {
        int listSize = (list == null) ? 0 : list.size();
        int mapSize = (map == null) ? 0 : map.size();
        return "Элементов в ArrayList: " + listSize + ", в HashMap: " + mapSize;
    }
}
