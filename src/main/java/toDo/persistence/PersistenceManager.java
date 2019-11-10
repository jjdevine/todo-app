package toDo.persistence;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PersistenceManager {

    private static final String PREFIX_NEXTPROP = "###NEXTPROP###";
    private static final String PREFIX_END = "###END###";

    public static void persist(PersistenceModel persistenceModel) throws IOException{

        validatePersistenceModel(persistenceModel);

        File directory = getDirectoryForType(persistenceModel.getType());

        if(!directory.exists()) {
            if (!directory.mkdir()) {
                throw new IOException("Unable to create save directory");
            }
        }

        String fileName =
                directory.getAbsolutePath()
                        + "/"
                        + persistenceModel.getSourceClass()
                        + "__"
                        + persistenceModel.getId();

        File file = new File(fileName);


        writeToFile(persistenceModel, file);
    }

    public static List<PersistenceModel> loadAllPersistenceModelsByType(String type) throws IOException {

        List<PersistenceModel> persistenceModels = new ArrayList<>();
        File directory = getDirectoryForType(type);

        for(File f: directory.listFiles()) {
            try {
                persistenceModels.add(readFromFile(f));
            } catch (IOException ex) {
                //rather than failing if a single file fails, just skip to next one
                ex.printStackTrace();
            }
        }

        return persistenceModels;
    }

    public static void deletePersistenceModel(PersistenceModel persistenceModel) throws IOException{

        File directory = getDirectoryForType(persistenceModel.getType());

        File fileToDelete = null;

        for(File file: directory.listFiles()) {
            int underscoreIndex = file.getName().lastIndexOf("_");

            if(underscoreIndex == -1) {
                //not correct filename format, ignore
                continue;
            }

            String id = file.getName().substring(underscoreIndex+1);

            if (id.equals(persistenceModel.getId())) {
                file.delete();
                break;
            }
        }
    }

    private static File getDirectoryForType(String type) throws IOException {
        File directory = new File("./" + type);

        if(!directory.exists()) {
            if (!directory.mkdir()) {
                throw new IOException("Unable to create save directory");
            }
        }

        return directory;
    }

    private static PersistenceModel readFromFile(File f) throws IOException {

        PersistenceModel persistenceModel = new PersistenceModel();

        try (BufferedReader in = new BufferedReader(new FileReader(f))) {

            String line;
            int index = 0;
            String currentPropertyKey = null;
            StringBuilder currentPropertyValue = new StringBuilder();

            while((line = in.readLine()) != null) {

                switch (index) {
                    case 0:
                        persistenceModel.setId(line.split("=")[1]);
                        break;
                    case 1:
                        persistenceModel.setType(line.split("=")[1]);
                        break;
                    case 2:
                        persistenceModel.setSourceClass(line.split("=")[1]);
                        break;
                    default:
                        //processing properties

                        if(line.startsWith(PREFIX_NEXTPROP)) { //new property
                            if(currentPropertyKey != null) {  //check not first property
                                //save previous property
                                persistenceModel.addProperty(currentPropertyKey, currentPropertyValue.toString());
                                currentPropertyValue = new StringBuilder();
                            }

                            currentPropertyKey = line.substring(PREFIX_NEXTPROP.length(), line.indexOf("="));
                            currentPropertyValue.append(line.substring(line.indexOf("=")+1));
                        } else if(line.startsWith(PREFIX_END)) {
                            //end of file, store final property
                            persistenceModel.addProperty(currentPropertyKey, currentPropertyValue.toString());
                            currentPropertyValue = new StringBuilder();
                        } else {
                            //continuation of multi-line property value
                            currentPropertyValue.append("\n");
                            currentPropertyValue.append(line);
                        }

                        break;
                }

                index++;
            }
        }

        return persistenceModel;
    }

    private static void writeToFile(PersistenceModel persistenceModel, File file) throws IOException {

        try (BufferedWriter out = new BufferedWriter(new FileWriter(file))) {

            out.write("id=" + persistenceModel.getId()+"\n");
            out.write("type=" + persistenceModel.getType()+"\n");
            out.write("sourceClass=" + persistenceModel.getSourceClass()+"\n");
            writeProperties(persistenceModel.getProperties(), out);
            out.write(PREFIX_END);
            out.flush();
        }
    }

    private static void writeProperties(Map<String, String> properties, BufferedWriter out) throws IOException {

        for(String key: properties.keySet()) {
            out.write(PREFIX_NEXTPROP + key + "=" + properties.get(key) + "\n");
        }
    }

    private static void validatePersistenceModel(PersistenceModel persistenceModel) throws IOException {

        if (isNullOrBlank(persistenceModel.getType())) {
            throw new IOException("Type must not be null or blank");
        }

        if (isNullOrBlank(persistenceModel.getId())) {
            throw new IOException("Id must not be null or blank");
        }

        if(persistenceModel.getSourceClass() == null) {
            throw new IOException("Source class must not be null or blank");
        }

        if(persistenceModel.getProperties() == null || persistenceModel.getProperties().size() == 0) {
            throw new IOException("Must provide at least 1 property to persist");
        }
    }

    private static boolean isNullOrBlank(String string) {
        return string == null || string.trim().length() == 0;
    }

}
