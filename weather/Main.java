
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Manusporn Fukkham
 */
public class Main {

    public static void main(String[] args) {
        File xmlFile = new File("weather/weather.xml");
        List<String> stringList = new ArrayList<>();
        try {
            Scanner scannerFile = new Scanner(new FileReader(xmlFile));
            while (scannerFile.hasNext()){
                String line = scannerFile.nextLine();
                line = line.replaceAll("^ +| +$|( )+", "$1");
                stringList.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String line = String.join("",stringList);

        // remove all comment or header of XML and split all the string cover by `<>`
        line = line.replaceAll("(<!--[\\s\\S\\n]*?-->)|(<\\?[\\s\\S\\n]*?\\?>)","");
        String[] lines = line.split("((?=<)|(?<=>))");

        List<XMLManager> objectXML =  splitXMLObject(lines);
    }

    private static List<XMLManager> splitXMLObject(String...stringList){
        for (String string: stringList){
            System.out.println(string);
        }

        return null;
    }
}
