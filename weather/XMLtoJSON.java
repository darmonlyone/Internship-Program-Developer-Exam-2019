import java.io.*;
import java.util.*;

/**
 * XMLtoJSON class to rewrite Xml file to jason
 * This application use for Internship Exam 2019
 * @author Manusporn Fukkham
 * @since 30/1/2019
 */
public class XMLtoJSON {

    /**
     * Main method of rewrite xml to json file.
     */
    public static void main(String[] args) {
        Scanner scannerText = new Scanner(System.in);

        System.out.print("Input your XML file path: ");
        String filePath = scannerText.nextLine();
        File xmlFile = new File(filePath);

        List<String> stringList = new ArrayList<>();
        try {
            if (!filePath.contains(".xml")) throw new FileNotFoundException();
            Scanner scannerFile = new Scanner(new FileReader(xmlFile));
            while (scannerFile.hasNext()){
                String line = scannerFile.nextLine();
                line = line.replaceAll("^ +| +$|( )+", "$1");
                stringList.add(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Input path was incorrect!");
            System.exit(0);
        }

        String XMLLine = String.join("",stringList);

        // remove all comment and header of XML and split all the string cover by `<>`
        XMLLine = XMLLine.replaceAll("(<!--[\\s\\S\\n]*?-->)|(<\\?[\\s\\S\\n]*?\\?>)","")
                .replaceAll("[<]\\s+","<")
                .replaceAll("[</]\\s+","</");
        String[] XMLLines = XMLLine.split("((?=<)|(?<=>))");

        List<String> listXML = Arrays.asList(XMLLines);

        if (removeCover(listXML.get(0)).equals(removeCover(listXML.get(listXML.size()-1)).replaceAll("/",""))){
            listXML.set(0 ,"{");
            listXML.set(listXML.size()-1,"}");
        }else{
            System.out.println("XML object was incorrect form");
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String string: listXML){
            string = string.replace(">", " />").replace("//","/");
            if (!string.contains("<") && !string.contains(">") && string.length() != 1){
                stringBuilder.delete(stringBuilder.length()-2,stringBuilder.length()-1);
            }
            if (string.length() == 1)
                stringBuilder.append(string);
            else {
                if (string.contains("</") && string.contains(">")){
                    stringBuilder.append("}");
                }else {
                    String word = removeCover(string).replaceAll("/", "");
                    String[] splited = word.split(" ");
                    String[] splited2 = string.split(" ");
                    stringBuilder.append(stringType(splited[0]).replaceAll("/", ""));
                    if ((!string.contains("/") || splited2[0].contains("<")) && string.contains(">")) {
                        stringBuilder.append(": {");
                    }
                    if (splited.length > 1) {
                        boolean isFirst = true;
                        int doubleCordCount = 0;
                        for (int i = 0; i < word.length(); i++) {
                            if (word.charAt(i) == ' ') {
                                isFirst = false;
                            }
                            if (!isFirst) {
                                if (doubleCordCount != 2) {
                                    if (word.charAt(i) == '=') {
                                        stringBuilder.append("\":  ");
                                    } else if (word.charAt(i) == '\"') {
                                        doubleCordCount++;
                                        stringBuilder.append("\"");
                                    } else if (word.charAt(i) == ' ' && doubleCordCount != 0) {
                                        stringBuilder.append(word.charAt(i));
                                    } else {
                                        stringBuilder.append(word.charAt(i));
                                    }
                                } else {
                                    doubleCordCount = 0;
                                    stringBuilder.append(",");
                                }
                                if (doubleCordCount == 0 && word.charAt(i) == ' ') {
                                    stringBuilder.append("\"");
                                }
                            }
                        }
                    }
                }
                if (!string.contains("<") && !string.contains(">")){
                    stringBuilder.append("~");
                }
            }
        }

        String refomatString = stringBuilder.toString();
        refomatString = refomatString.replace(":{",":")
                .replace(" \"","\"")
                .replace("\"\"\"","},\"")
                .replace("\"\"}","}},")
                .replace("\"\"","\"")
                .replace(",}","}")
                .replace("}\"","},\"")
                .replace("~}","");
        StringBuilder reformatStringBuilder = new StringBuilder(refomatString);
        while (reformatStringBuilder.toString().charAt(reformatStringBuilder.length()-1) != '}')
            reformatStringBuilder.deleteCharAt(reformatStringBuilder.length()-1);
        refomatString = reformatStringBuilder.toString();
        refomatString = refomatString.replaceAll(",", ",\n").replaceAll("\\{","{\n").replaceAll("\\}","\n}");

        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            filePath = filePath.replace("xml","json");
            fileWriter = new FileWriter(filePath);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(refomatString);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert bufferedWriter != null;
                bufferedWriter.close();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Out put: " + filePath);
    }

    /**
     * Replace string to cover with string form.
     * @param string some string
     * @return string form
     */
    private static  String stringType(String string){
        return "\""+string+"\"";
    }

    /**
     * Remove xml object covered string.
     * @param str String of xml object
     * @return string of removed cover be xml form
     */
    private static String removeCover(String str){
        return str.replaceAll("<","").replaceAll(">","").trim();
    }
}
