import java.util.*;
import java.io.*;
public class Main {
    public static void main(String[] args) throws EmptyAutosarFileException, NotValidExtension{
        try {
            String filename = args[0];
            if (!filename.endsWith(".arxml")) {
                throw new NotValidExtension("Invalid File Extension");
            }
            File file = new File(filename);
            FileInputStream inputStream = new FileInputStream(file);
            int d;
            StringBuilder stringBuilder = new StringBuilder();
            while((d=inputStream.read()) != -1){
                stringBuilder.append((char) d);
            }
            String data = stringBuilder.toString();
            Scanner sc = new Scanner(data);
            ArrayList<Container> containers = new ArrayList<>();
            if(!sc.hasNextLine()) {
                throw new EmptyAutosarFileException("Empty Autosar File");
            }
            while(sc.hasNextLine()){
                String line = sc.nextLine();
                if(line.contains("<CONTAINER")){
                    String UUID = line.substring(line.indexOf("\"")+1,line.lastIndexOf("\""));
                    String snTag = sc.nextLine();
                    String shortName = snTag.substring(snTag.indexOf(">")+1, snTag.indexOf("</"));
                    String lnTag = sc.nextLine();
                    String longName = lnTag.substring(lnTag.indexOf(">")+1, lnTag.indexOf("</"));
                    Container c = new Container(UUID,shortName,longName);
                    containers.add(c);
                }
            }
            Collections.sort(containers);
            String outName = filename.substring(0,filename.indexOf(".")) + "_mod.arxml";
            FileOutputStream outputStream = new FileOutputStream(outName);
            outputStream.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n".getBytes());
            outputStream.write("<AUTOSAR>\n".getBytes());
            for(int i = 0; i < containers.size(); i++){
                outputStream.write(containers.get(i).toString().getBytes());
            }
            outputStream.write("</AUTOSAR>".getBytes());
        } catch (FileNotFoundException e){
            e = new FileNotFoundException();
        } catch (IOException e){
            e = new IOException();
        }
    }
}
class NotValidExtension extends Exception{
    public NotValidExtension(String message){
        super(message);
    }
}
class EmptyAutosarFileException extends Exception{
    public EmptyAutosarFileException(String message){
        super(message);
    }
}
class Container implements Comparable<Container>{
    private String UUID;
    private String shortName;
    private String longName;
    public Container(String UUID, String shortN, String longN){
        this.UUID = UUID;
        this.shortName = shortN;
        this.longName = longN;
    }
    public String getUUID(){
        return UUID;
    }
    public String getShortName(){
        return shortName;
    }
    public String getLongName(){
        return longName;
    }

    @Override
    public int compareTo(Container o) {
        if(this.shortName.charAt(9) > o.getShortName().charAt(9)){
            return 1;
        }else if(this.shortName.charAt(9) < o.getShortName().charAt(9)){
            return -1;
        }else{
            return 0;
        }
    }
    public String toString(){
        return ("    <CONTAINER UUID=\"" + this.UUID + "\">\n"
                + "        <SHORT-NAME>" + this.shortName + "</SHORT-NAME>\n"
                + "        <LONG-NAME>" + this.longName + "</LONG-NAME>\n"
                + "    </CONTAINER>\n");
    }
}