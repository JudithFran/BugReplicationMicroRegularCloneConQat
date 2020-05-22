import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 *
 * @author Judith
 */
public class InputParameters {
    public static String systemName = "", path = "", programmingLanguage = "";
    public static int lastRevision;

    public void setParameters(String sysName){
        try{
            int flag = 0;

            if(sysName.equals("Ctags")){
                systemName = sysName;
                lastRevision = 774;
                path = "C:/Deckard_Clones/Ctags/Repository/version-";
                programmingLanguage = "C";
            }
            else if(sysName.equals("Brlcad")){
                systemName = sysName;
                lastRevision = 735;
                path = "C:/Deckard_Clones/Brlcad/Repository/version-";
                programmingLanguage = "C";
            }
            else if(sysName.equals("Freecol")){
                systemName = sysName;
                lastRevision = 1950;
                path = "C:/Deckard_Clones/Freecol/Repository/version-";
                programmingLanguage = "Java";
            }
            else if(sysName.equals("Carol")){
                systemName = sysName;
                lastRevision = 1700;
                path = "C:/Deckard_Clones/Carol/Repository/version-";
                programmingLanguage = "Java";
            }
            else if(sysName.equals("Jabref")){
                systemName = sysName;
                lastRevision = 1545;
                path = "C:/ConQat_Clones/Jabref/version-";
                programmingLanguage = "Java";
            }
            else if(sysName.equals("Select")){
                System.out.println("Please select a valid subject system.");
                flag = 1;
            }

            if(flag == 0)
                System.out.println("This is inside setParameters systemName = " + systemName + " Programming language = " + programmingLanguage + " Path = " + path + " Last revision = " + lastRevision);

        }catch(Exception e){
            System.out.println("Error in method setParameters = " + e);
            e.printStackTrace();
        }
    }

    public void getParameters(){
        try{
            System.out.println("Inside getParameters, System name = " + systemName);
        }catch(Exception e){
            System.out.println("Error in method getParameters = " + e);
            e.printStackTrace();
        }
    }
}
