import java.io.*;
import java.util.ArrayList;


/**
 *
 * @author Judith
 */

class CodeFragment {

    int revision = -1;
    String filepath = "";
    int startline = -1, endline = -1;
    String changetype = "-1";

    String[] lines = new String[5000];

    public void getFragment() {

        String abs_filepath = InputParameters.path + revision + "/" + filepath;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(abs_filepath)));
            String str = "";

            int line = 0;
            int i = 0;
            while ((str = br.readLine()) != null) {
                line++;
                if (line > endline) {
                    break;
                }
                if (line >= startline && line <= endline) {
                    lines[i] = str.trim();
                    //System.out.println("lines[" + i + "] = " + lines[i]);
                    i++;
                }
            }
        } catch (Exception e) {
            System.out.println("error.getFragment." + e);
        }

    }

    public void showFragment() {

        String abs_filepath = InputParameters.path + revision + "/" + filepath;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(abs_filepath)));
            String str = "";

            System.out.println("\n" + revision + ": " + filepath + ", " + startline + " - " + endline);
            //System.out.println("---------------------------------------------------");
            int line = 0;
            int i = 0;
            while ((str = br.readLine()) != null) {
                line++;
                if (line > endline) {
                    break;
                }
                if (line >= startline && line <= endline) {
                    lines[i] = str.trim();
                    i++;
                    //System.out.println(str);
                }
            }
            //System.out.println("---------------------------------------------------");
        } catch (Exception e) {
            System.out.println("error.showFragment." + e);
        }
    }
}


public class BugReplicationMicroRegularClones {

    CodeFragment[][] cfp = new CodeFragment[100000][2];

    DBConnect db = new DBConnect();
    CompareChanges cc = new CompareChanges();

    int countRevR = 0;
    int countRevRepR = 0;

    int countRevM = 0;
    int countRevRepM = 0;

    int RQ4 = 0;

    public String getBugFixCommits() {
        String bugFixCommits = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(InputParameters.systemName + " commitlog.txt")));

            String str = "";
            String prevString = "";

            int commit = 0;
            while ((str = br.readLine()) != null) {
                if (str.trim().length() == 0) {
                    continue;
                }

                if (prevString.contains("--------------------------------")) {
                    //this is the starting of a commit report.
                    //we need to know the commit number.
                    String str1 = str.trim().split("[ ]+")[0].trim();
                    str1 = str1.substring(1);
                    commit = Integer.parseInt(str1);
                    //System.out.println ("Testing for Deckard version. Commit = " + commit);
                } else {
                    //according to the study of Mockus
                    if (str.toLowerCase().contains("bug") || str.toLowerCase().contains("fix") || str.toLowerCase().contains("fixup") || str.toLowerCase().contains("error") || str.toLowerCase().contains("fail"))
                    //if (str.contains ("bug") || str.contains("fix") || str.contains ("fixup") || str.contains ("error") || str.contains ("fail"))
                    {
                        if (!bugFixCommits.contains(" " + commit + " ")) {
                            bugFixCommits += " " + commit + " ";
                        }
                    }
                }
                prevString = str;
            }
            br.close();
            //System.out.println ("Revisions that were created because of a bug fix = " + bugFixCommits);

        } catch (Exception e) {
            System.out.println("Error in getBugFixCommits = " + e);
        }

        return bugFixCommits;
    }

    // For RQ4 use this method

    public String getBugFixCommitsRQ4() {
        String bugFixCommits = "";
        try{
            String[] bugFixCommitsMockus = new String[10000];
            String[] bugFixCommitsLamkanfi = new String[10000];
            String[] bugFixCommitsTemp = new String[10000];

            String str1 = getBugFixCommitsMockus();
            System.out.println ("Revisions that were created because of a bug fix (Mockus) = " + str1);
            bugFixCommitsMockus = str1.trim().split("  ");

            for(int i = 0; i < bugFixCommitsMockus.length; i++)
                System.out.println ("Revisions that were created because of a bug fix in bugFixCommitsMockus["+i+"] array (Mockus) = " + bugFixCommitsMockus[i]);

            String str2 = getBugFixCommitsLamkanfi();
            System.out.println ("Revisions that were created because of a bug fix (Lamkanfi) = " + str2);
            bugFixCommitsLamkanfi = str2.trim().split("  ");

            for(int i = 0; i < bugFixCommitsLamkanfi.length; i++)
                System.out.println ("Revisions that were created because of a bug fix in bugFixCommitsLamkanfi["+i+"] array (Lamkanfi) = " + bugFixCommitsLamkanfi[i]);

            // Finding common commits in both arrays

            for (int i = 0; i < bugFixCommitsMockus.length; i++) {
                for (int j = 0; j < bugFixCommitsLamkanfi.length; j++) {
                    if (bugFixCommitsMockus[i].equals(bugFixCommitsLamkanfi[j])) {
                        // got the duplicate element
                        //if (!bugFixCommits.contains(" " + bugFixCommitsMockus[i] + " ")) {
                        bugFixCommits += " " + bugFixCommitsMockus[i] + " ";
                        //}
                    }
                }
            }
            System.out.println ("Revisions that were created because of a bug fix (Mockus and Lamkanfi) = " + bugFixCommits);

            bugFixCommitsTemp = bugFixCommits.trim().split("  ");

            for(int i = 0; i < bugFixCommitsTemp.length; i++)
                System.out.println ("Revisions that were created because of a bug fix in bugFixCommitsTemp["+i+"] array (Temp) = " + bugFixCommitsTemp[i]);


        } catch (Exception e) {
            System.out.println("Error in getBugFixCommits = " + e);
        }

        return bugFixCommits;
    }

    public String getBugFixCommitsMockus() {
        String bugFixCommits = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(InputParameters.systemName + " commitlog.txt")));

            String str = "";
            String prevString = "";

            int commit = 0;
            while ((str = br.readLine()) != null) {
                if (str.trim().length() == 0) {
                    continue;
                }

                if (prevString.contains("--------------------------------")) {
                    //this is the starting of a commit report.
                    //we need to know the commit number.
                    String str1 = str.trim().split("[ ]+")[0].trim();
                    str1 = str1.substring(1);
                    commit = Integer.parseInt(str1);
                    //System.out.println (commit);
                } else {
                    //according to the study of Mockus
                    if (str.toLowerCase().contains("bug") || str.toLowerCase().contains("fix") || str.toLowerCase().contains("fixup") || str.toLowerCase().contains("error") || str.toLowerCase().contains("fail"))
                    //if (str.contains ("bug") || str.contains("fix") || str.contains ("fixup") || str.contains ("error") || str.contains ("fail"))
                    {
                        if (!bugFixCommits.contains(" " + commit + " ")) {
                            bugFixCommits += " " + commit + " ";
                        }
                    }
                }
                prevString = str;
            }
            br.close();
            //System.out.println ("Revisions that were created because of a bug fix = " + bugFixCommits);

        } catch (Exception e) {
            System.out.println("Error in getBugFixCommitsMockus = " + e);
        }

        return bugFixCommits;
    }

    public String getBugFixCommitsLamkanfi() {
        String bugFixCommits = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(InputParameters.systemName + " commitlog.txt")));

            String str = "";
            String prevString = "";

            int commit = 0;
            while ((str = br.readLine()) != null) {
                if (str.trim().length() == 0) {
                    continue;
                }

                if (prevString.contains("--------------------------------")) {
                    //this is the starting of a commit report.
                    //we need to know the commit number.
                    String str1 = str.trim().split("[ ]+")[0].trim();
                    str1 = str1.substring(1);
                    commit = Integer.parseInt(str1);
                    //System.out.println (commit);
                } else {

                    // Severe bugs according to Lamkanfi
                    if (str.toLowerCase().contains("fault") || str.toLowerCase().contains("machin") || str.toLowerCase().contains("reboot") || str.toLowerCase().contains("reinstal") || str.toLowerCase().contains("lockup") || str.toLowerCase().contains("seemingli") || str.toLowerCase().contains("perman") || str.toLowerCase().contains("instantli") || str.toLowerCase().contains("segfault") || str.toLowerCase().contains("compil")
                            || str.toLowerCase().contains("hang") || str.toLowerCase().contains("freez") || str.toLowerCase().contains("deadlock") || str.toLowerCase().contains("thread") || str.toLowerCase().contains("slow") || str.toLowerCase().contains("anymor") || str.toLowerCase().contains("memori") || str.toLowerCase().contains("tick") || str.toLowerCase().contains("jvm") || str.toLowerCase().contains("adapt")
                            || str.toLowerCase().contains("deadlock") || str.toLowerCase().contains("sigsegv") || str.toLowerCase().contains("relat") || str.toLowerCase().contains("caus") || str.toLowerCase().contains("snapshot") || str.toLowerCase().contains("segment") || str.toLowerCase().contains("core") || str.toLowerCase().contains("unexpectedli") || str.toLowerCase().contains("build") || str.toLowerCase().contains("loop"))
                    {
                        if (!bugFixCommits.contains(" " + commit + " ")) {
                            bugFixCommits += " " + commit + " ";
                        }
                    }
                }
                prevString = str;
            }
            br.close();
            //System.out.println ("Revisions that were created because of a bug fix = " + bugFixCommits);

        } catch (Exception e) {
            System.out.println("Error in getBugFixCommitsLamkanfi = " + e);
        }

        return bugFixCommits;
    }

    public CodeFragment[][] getChangedBugFixCommits() {

        SingleChange[] changedBugFixCommits = new SingleChange[50000];
        SingleChange[][] changedBugFixCommits2D = new SingleChange[5000][5000]; // was 10000 before optimization
        CodeFragment[][] changedBugFixCommits2DNew = new CodeFragment[5000][5000];  // was 10000 before optimization

        try {
            String str = "";

            if (RQ4 == 0)
                str = getBugFixCommits();
            else if (RQ4 == 1)
                str = getBugFixCommitsRQ4();

            String[] bugFixCommits = new String[10000];

            //SingleChange[] changes = new SingleChange[10000];
            SingleChange[] changes = db.getChangedRevisions();

            /*
            for(int j=0; changes[j] != null; j++){
            System.out.println("Revision [" + j + "]= " + changes[j].revision);
            }
             */
            bugFixCommits = str.split("  ");

            /*---------------------------------------- Preprocessing bugFixCommits Start ---------------------------------------------------------------*/
            String[] bugFixCommitsReverse = new String[10000];
            int i = 0;
            for (int j = bugFixCommits.length - 1; j >= 0; j--) {
                bugFixCommitsReverse[i] = bugFixCommits[j];
                //System.out.println("Bug Fix Revision [" + i + "] in Reverse = " + bugFixCommitsReverse[i] + " Where j = " + j);
                i++;
            }
            int len = i;

            //Changing x to x-1 for revision numbers
            for (i = 0; i < len; i++) {
                bugFixCommitsReverse[i] = Integer.toString(Integer.parseInt(bugFixCommitsReverse[i].trim()) - 1);
                //System.out.println("Bug Fix Revision [" + i + "] decreased value by 1 = " + bugFixCommitsReverse[i]);
            }
            /*---------------------------------------- Preprocessing bugFixCommits End ---------------------------------------------------------------*/

            //Matching bug-fix commits with changed revisions and saving in 1D array

            int k = 0;
            for (i = 0; i < len; i++) {
                for (int j = 0; changes[j] != null; j++) {
                    if (bugFixCommitsReverse[i].equals(changes[j].revision)) {
                        changedBugFixCommits[k] = changes[j];
                        //System.out.println("Revision [" + k + "] in changedBugFixCommits = " + changedBugFixCommits[k].revision);
                        k++;
                    }
                }
            }

            //Matching bug-fix commits with changed revisions and saving in 2D array
            int a = 0, b = 0;
            for (i = 0; i < len; i++) {
                for (int j = 0; changes[j] != null; j++) {
                    if (bugFixCommitsReverse[i].equals(changes[j].revision)) {
                        changedBugFixCommits2D[a][b] = changes[j];
                        if(changes[j+1] != null){
                            if(changes[j].revision.equals(changes[j+1].revision)){
                                b++;
                                changedBugFixCommits2D[a][b] = changes[j+1];
                            }
                            else
                                a++;
                        }
                    }
                }
                b = 0;
            }
            // Changing the data type from SingleChange to CodeFragment of the 2D array
            for(i = 0; i<changedBugFixCommits2D.length; i++){
                for(int j = 0; j<changedBugFixCommits2D.length; j++){
                    if(changedBugFixCommits2D[i][j] != null){
                        changedBugFixCommits2DNew[i][j] = new CodeFragment();
                        changedBugFixCommits2DNew[i][j].revision = Integer.parseInt(changedBugFixCommits2D[i][j].revision);
                        changedBugFixCommits2DNew[i][j].startline = Integer.parseInt(changedBugFixCommits2D[i][j].startline);
                        changedBugFixCommits2DNew[i][j].endline = Integer.parseInt(changedBugFixCommits2D[i][j].endline);
                        changedBugFixCommits2DNew[i][j].filepath = changedBugFixCommits2D[i][j].filepath;
                        changedBugFixCommits2DNew[i][j].changetype = changedBugFixCommits2D[i][j].changetype;
                    }
                }
            }

            int count = 0;
            for(i = 0; i<changedBugFixCommits2D.length; i++){
                for(int j = 0; j<changedBugFixCommits2D.length; j++){
                    if(changedBugFixCommits2D[i][j] != null){
                        System.out.println("getChangedBugFixCommits: getChangedBugFixCommits["+i+"]["+j+"].revision = " + changedBugFixCommits2D[i][j].revision
                                + " Filepath = " + changedBugFixCommits2D[i][j].filepath + " Startline = " + changedBugFixCommits2D[i][j].startline
                                + " Endline = " + changedBugFixCommits2D[i][j].endline + " Changetype = " + changedBugFixCommits2D[i][j].changetype);
                        count++;
                    }
                    else
                        break;
                }
            }
            System.out.println("Total number of changed bug-fix code fragments (CF) = " + count);

        } catch (Exception e) {
            System.out.println("Error in getChangedBugFixCommits = " + e);
        }
        return changedBugFixCommits2DNew;
    }

    public void bugReplication(){
        try{
            // --------------------------Implementing RQ1----------------------------

            int countFragmentR = 0;
            int countFragmentM = 0;
            int countRevision = 0;

            ArrayList<CodeFragment> bugRepR = new ArrayList<>();
            ArrayList<CodeFragment> bugRepM = new ArrayList<>();


            System.out.println("---------------------------------------Regular Clone Analysis Starts Here--------------------------------------------\n");
            bugRepR = bugReplicationR();
            countFragmentR = bugRepR.size();

            System.out.println("-----------------------------------------Micro Clone Analysis Starts Here--------------------------------------------\n");
            //bugRepM = bugReplicationM();
            countFragmentM = bugRepM.size();

            // Remove all duplicates
            bugRepR.removeAll(bugRepM);

            // Merge two arraylists
            bugRepR.addAll(bugRepM);

            System.out.println("\nThis is the array of replicated bugs after UNION operation in between Regular and Micro: ");
            for(int i=0; i<bugRepR.size(); i++){
                //System.out.println("\nThis is the array of replicated bugs after UNION operation in between Regular and Micro: i = " + i);
                bugRepR.get(i).getFragment();
                bugRepR.get(i).showFragment();
            }

            // Removing the duplicate values based on revisions only
            // Removing duplicate revisions so that we can count the distinct revisions only
            for(int i = 0; i < bugRepR.size(); i++){
                for(int j = i+1; j < bugRepR.size(); j++){

                    if(bugRepR.get(i).revision == bugRepR.get(j).revision){
                        bugRepR.remove(j);
                        j--;
                    }
                }
            }

            System.out.println("\nThis is the array of replicated bugs after UNION operation in between Regular and Micro and after removing duplicate revision number: ");
            for(int i=0; i<bugRepR.size(); i++){
                //System.out.println("\nThis is the array of replicated bugs after UNION operation in between Regular and Micro and after removing duplicate revision number: i = " + i);
                bugRepR.get(i).getFragment();
                bugRepR.get(i).showFragment();
            }

            countRevision = bugRepR.size();

            // Answering RQ1
            // Calculating the average number of replicated bugs for both regular and micro clones

            System.out.println("Total number of distinct clone fragments that experienced bug-replication in Regular clones = " + countFragmentR);
            System.out.println("Total number of distinct clone fragments that experienced bug-replication in Micro clones = " + countFragmentM);
            System.out.println("Total number of distinct revisions that experienced bug-replication in both Regular and Micro clones = " + countRevision);

            System.out.println("Distinct percentage of replicated bugs in regular clones per revision = " + (float) countFragmentR/countRevision);
            System.out.println("Distinct percentage of replicated bugs in micro clones per revision = " + (float) countFragmentM/countRevision);

        } catch(Exception e){
            System.out.println("error in BugReplication = " + e);
            e.printStackTrace();
        }
    }

    public ArrayList<CodeFragment> bugReplicationR(){
        ArrayList<CodeFragment> bugRep = new ArrayList<>();
        try{
            //CodeFragment[][] cloneFragmentPair = new CodeFragment[10000][2]; // was 10000 before optimization

            //cloneFragmentPair = isClonePair();

            //cloneFragmentPair = cfp;

            isClonePair();

            if(cfp != null)
                for (int i = 0; i < cfp.length; i++)
                    for (int j = 0; j < 2; j++)
                        if(cfp[i][j] != null)
                            System.out.println("bugReplicationR: cfp["+i+"]["+j+"].revision = " + cfp[i][j].revision + " Filepath = " + cfp[i][j].filepath
                                    + " Startline = " + cfp[i][j].startline + " Endline = " + cfp[i][j].endline);
                        else
                            break;

            // For RQ3
            int countRevision = 1;
            if(cfp != null)
                for (int i = 0; cfp[i][0] != null; i++)
                    if(cfp[i+1][0] != null)
                        if(cfp[i][0].revision != cfp[i+1][0].revision)
                            countRevision++;


            System.out.println("Total number of distinct bugs(revision) of code clones for Regular = " + countRevision);

            countRevR = countRevision;


            // Finding Replicated Bugs
            int numReplicatedBugFixCommits = 0;
            for(int x = 0; cfp[x][0] != null; x++){
                if(cfp[x][0] != null && cfp[x][1] != null){
                    CodeFragment[] cloneFragmentPairINR = new CodeFragment[2];

                    cloneFragmentPairINR[0] = getInstanceInNextRevision(cfp[x][0]);
                    cloneFragmentPairINR[1] = getInstanceInNextRevision(cfp[x][1]);

                    if(cloneFragmentPairINR[0] != null && cloneFragmentPairINR[1] != null){
                        if(isClonePairBinary(cloneFragmentPairINR[0], cloneFragmentPairINR[1]) == 1){
                            numReplicatedBugFixCommits++;
                            System.out.println("///////////////////////////////////////////////////////////////////////Replicated Bug Fixing Change Found (Regular)///////////////////////////////////////////////////////////////////////");
                            //System.out.println("numReplicatedBugFixCommits for Regular Clones = " + numReplicatedBugFixCommits);

                            bugRep.add(cfp[x][0]);
                            bugRep.add(cfp[x][1]);
                        }
                    }
                }
            }
            System.out.println("Total Number of Pairs of Replicated Bug-Fix Commits for Regular Clones = " + numReplicatedBugFixCommits);

            System.out.println("\nThis is the array of replicated bugs: ");
            for(int i=0; i<bugRep.size(); i++){
                //System.out.println("\nThis is the array of replicated bugs: i = " + i);
                bugRep.get(i).getFragment();
                bugRep.get(i).showFragment();
            }

            // Removing the duplicate values
            for(int i = 0; i < bugRep.size(); i++){
                for(int j = i+1; j < bugRep.size(); j++){

                    if(bugRep.get(i).revision == bugRep.get(j).revision && bugRep.get(i).filepath.equals(bugRep.get(j).filepath)
                            && bugRep.get(i).startline == bugRep.get(j).startline && bugRep.get(i).endline == bugRep.get(j).endline){
                        bugRep.remove(j);
                        j--;
                    }
                }
            }

            System.out.println("\nThis is the array of replicated bugs after removing duplicate objects: ");
            for(int i=0; i<bugRep.size(); i++){
                //System.out.println("\nThis is the array of replicated bugs after removing duplicate objects: i = " + i);
                bugRep.get(i).getFragment();
                bugRep.get(i).showFragment();
            }

            System.out.println("\nTotal distinct number of replicated bugs in regular code clone = " + bugRep.size()); // OUTPUT RESULT

            // Counting the distinct number replicated bug in regular clones per revision
            int countRevisionRep = 1;
            for(int i = 0; i < bugRep.size()-1; i++)
                if(bugRep.get(i).revision != bugRep.get(i+1).revision)
                    countRevisionRep++;

            // For RQ3
            System.out.println("\nTotal distinct number of replicated bug revision in regular code clone = " + countRevisionRep); // OUTPUT RESULT

            countRevRepR = countRevisionRep;

            float averageCountRegular = (float)bugRep.size()/countRevisionRep;

            System.out.println("\nDistinct number of replicated bugs in regular clones per revision = " + averageCountRegular + "\n"); // OUTPUT RESULT

            // Saving output result of regular clones to a text file.

            PrintWriter writer = new PrintWriter("Temp_output.txt", "UTF-8");

            writer.println("\nTotal distinct number of replicated bugs in regular code clone = " + bugRep.size());
            writer.println("\nTotal distinct number of replicated bug revision in regular code clone = " + countRevisionRep);
            writer.println("\nDistinct number of replicated bugs in regular clones per revision = " + averageCountRegular + "\n");

            writer.close();

        }catch(Exception e){
            System.out.println("error in BugReplicationR = " + e);
            e.printStackTrace();
        }
        return bugRep;
    }

    public void isClonePair(){
        //CodeFragment[][] cfp = new CodeFragment[5000][2];   // was 10000 before optimization
        try{
            CodeFragment[][] changedBugFixCommits = new CodeFragment[500][500];   // was 10000 before optimization
            changedBugFixCommits = getChangedBugFixCommits();

            CodeFragment[][] cfFile = new CodeFragment[10000][10000];
            CodeFragment[] cfFileMatch = new CodeFragment[100000];
            int x = 0;

            // Looping through the changed bug-fix commit 2D array
            for(int i = 0; i<changedBugFixCommits.length; i++){
                for(int j = 0; j<changedBugFixCommits.length; j++){
                    if(changedBugFixCommits[i][j] != null){
                        //System.out.println("Revision number = " + changedBugFixCommits[i][j].revision);
                        cfFile = fileRead(changedBugFixCommits[i][j].revision);


                        // Looping through the output file of each revision
                        for (int m = 0; m < cfFile.length; m++) {
                            for (int n = 0; n < cfFile.length; n++) {

                                if (cfFile[m][n] != null){

                                    if(changedBugFixCommits[i][j].filepath.equals(cfFile[m][n].filepath)){

                                        // Checking for matches with changed bug-fix commits with each line of clone file (Deckard output file) of a particular revision
                                        // Matches if line numbers of code fragment from changedBugFixCommits are overlapping with line numbers range of cfFile
                                        if(((changedBugFixCommits[i][j].startline >= cfFile[m][n].startline) && (changedBugFixCommits[i][j].endline <= cfFile[m][n].endline))
                                                ||((changedBugFixCommits[i][j].startline <= cfFile[m][n].startline) && (changedBugFixCommits[i][j].endline <= cfFile[m][n].endline)
                                                && (changedBugFixCommits[i][j].endline >= cfFile[m][n].startline))
                                                ||((changedBugFixCommits[i][j].startline >= cfFile[m][n].startline) && (changedBugFixCommits[i][j].endline >= cfFile[m][n].endline)
                                                && (changedBugFixCommits[i][j].startline <= cfFile[m][n].endline))){

                                            System.out.print("*********************************************** File Name matched (Regular) ***********************************************");
                                            System.out.println(" Revision number = " + changedBugFixCommits[i][j].revision);

                                            //System.out.println("Matched CF from changedBugFixCommits["+i+"]["+j+"] = " + changedBugFixCommits[i][j].filepath + " Start Line = "
                                            //+ changedBugFixCommits[i][j].startline + " End Line = " + changedBugFixCommits[i][j].endline);

                                            //System.out.println("Matched CF1 from cfFile["+m+"]["+n+"] = " + cfFile[m][n].filepath + " Start Line = " + cfFile[m][n].startline
                                            //+ " End Line = " + cfFile[m][n].endline);

                                            // Saving the matched entries into a separate 1D array
                                            cfFileMatch[x] = cfFile[m][n];
                                            x++;
                                        }
                                    }
                                }
                                else
                                    break;
                            }
                            if (cfFile[m][0] == null)
                                break;
                        }


                    }
                    else
                        break;
                }
                if(changedBugFixCommits[i][0] == null)
                    break;
            }
            int len = 0;
            for(x = 0; cfFileMatch[x] != null; x++){
                System.out.println("cfFileMatch["+x+"] Revision = " + cfFileMatch[x].revision + " Filepath = " + cfFileMatch[x].filepath
                        + " Startline = " + cfFileMatch[x].startline + " Endline = " + cfFileMatch[x].endline);
                len++;
            }
            System.out.println("len = " + len);

            // Delete duplicate values from cfFileMatch[x] array
            for(int i = 0; i < len; i++){
                for(int j = i+1; j < len; ){
                    if(cfFileMatch[i].revision == cfFileMatch[j].revision && cfFileMatch[i].filepath.equals(cfFileMatch[j].filepath)
                            && cfFileMatch[i].startline == cfFileMatch[j].startline && cfFileMatch[i].endline == cfFileMatch[j].endline){
                        for(x = j; x < len; x++){
                            cfFileMatch[x] = cfFileMatch[x+1];
                        }
                        len--;
                    }
                    else{
                        j++;
                    }
                }
            }

            System.out.println("After removing duplicate values: ");
            for(x = 0; cfFileMatch[x] != null; x++){
                System.out.println("cfFileMatch["+x+"] Revision = " + cfFileMatch[x].revision + " Filepath = " + cfFileMatch[x].filepath
                        + " Startline = " + cfFileMatch[x].startline + " Endline = " + cfFileMatch[x].endline);
            }

            int classID1 = 0, classID2 = 0;
            x = 0;
            for(int i = 0; cfFileMatch[i] != null; i++){
                for(int j = i+1; cfFileMatch[j] != null; j++){
                    if(cfFileMatch[i].revision == cfFileMatch[j].revision){
                        //System.out.println("Revision = " + cfFileMatch[i].revision);

                        classID1 = getClassID(cfFileMatch[i]);
                        //System.out.println("classID1 in Regular = " + classID1);

                        classID2 = getClassID(cfFileMatch[j]);
                        //System.out.println("classID2 in Regular = " + classID2 + "\n");

                        if(classID1 == classID2){
                            System.out.print("********************************************Pair Found (Regular)********************************************");
                            System.out.println(" Revision number = " + cfFileMatch[i].revision);
                            cfp[x][0] = cfFileMatch[i];
                            cfp[x][1] = cfFileMatch[j];
                            x++;
                        }
                    }
                }
            }

        }catch (Exception e) {
            System.out.println("Error in method isClonePair = " + e);
            e.printStackTrace();
        }
        //return cfp;
    }

    public CodeFragment[][] fileRead(int rev){

        // In this method I use cfFile (a two dimensional array) to store each clone file (Deckard clone output file). In first dimension it will store the class number (classID)
        // and in second dimension it will store each clone fragments.
        CodeFragment[][] cfFile = new CodeFragment[10000][10000];
        try{
            String[] store = new String[50000];
            String[][] store2D = new String[10000][10000];

            File regularFile = new File(InputParameters.path + rev + "/clusters/cluster_vdb_30_5_allg_0.95_30"); //All Type

            if(regularFile.exists()) {

                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(regularFile))); // All Type

                String str = "";
                int i = 0;

                while ((str = br.readLine()) != null) {

                    store[i] = str;
                    i++;
                }
                /*
                for(i = 0; i<store.length ; i++)
                    if(store[i] != null)
                        System.out.println("store[" + i + "] = " + store[i]);
                    else
                        break;
                */
                int m = 0;
                int n = 0;
                int flag = 0;

                for (i = 0; i < store.length; i++)
                    if (store[i] != null){
                        if (!store[i].isEmpty()) {
                            flag = 1;
                            store2D[m][n] = store[i];
                            n++;
                        }
                        else if (flag == 1) {
                            m++;
                            n = 0;
                            flag = 0;
                        }
                    }
                    else
                        break;
                /*
                for(i = 0; i<store2D.length ; i++)
                    for(int j = 0; j<store2D.length ; j++)
                        if(store2D[i][j] != null)
                            System.out.println("store2D[" + i + "][" + j + "] = " + store2D[i][j]);
                        else
                            break;
                */

                for (i = 0; i < store2D.length; i++) {
                    for (int j = 0; j < store2D.length; j++)
                        if (store2D[i][j] != null) {

                            cfFile[i][j] = new CodeFragment();
                            cfFile[i][j].revision = rev;

                            cfFile[i][j].filepath = store2D[i][j].split("[ ]+")[1].trim();
                            String[] filePath = cfFile[i][j].filepath.split("version-\\d*\\/");
                            cfFile[i][j].filepath = filePath[1];
                            //System.out.println("**********************Testing for Deckard version. This is inside fileRead method for testing.*******************************");
                            //System.out.println(" i = " + i + " j = " + j + " cfFile[i][j].filepath = " + cfFile[i][j].filepath);

                            cfFile[i][j].startline = Integer.parseInt(store2D[i][j].split("[:]+")[2].trim());
                            //System.out.println(" i = " + i + " j = " + j + " cfFile[i][j].startline = " + cfFile[i][j].startline);

                            String cs = store2D[i][j].split("[:]+")[3].trim();
                            int cloneSize = Integer.parseInt(cs.split("[ ]+")[0].trim());

                            if (cloneSize > 4) {
                                cfFile[i][j].endline = cfFile[i][j].startline + cloneSize;
                                //System.out.println(" i = " + i + " j = " + j + " cfFile[i][j].endline = " + cfFile[i][j].endline);
                            }
                        }
                        else
                            break;
                    if(store2D[i][0] == null)
                        break;
                }
            }
            /*
            for (int m = 0; m < cfFile.length; m++) {
                for (int n = 0; n < cfFile.length; n++) {
                    if (cfFile[m][n] != null) {
                        System.out.println("cfFile[" + m + "][" + n + "] = " + cfFile[m][n].filepath + " Start Line = " + cfFile[m][n].startline
                                + " End Line = " + cfFile[m][n].endline);
                    }
                    else
                        break;
                }
            }
            */
        } catch(Exception e){
            System.out.println("Error in method fileRead()." + e);
            e.printStackTrace();
        }
        return cfFile;

    }

    public int getClassID(CodeFragment cf){

        // In this method I use cfFile (a two dimensional array) to store each clone file (Deckard clone output file). In first dimension it will store the class number (classID)
        // and in second dimension it will store each clone fragments.
        CodeFragment[][] cfFile = new CodeFragment[1000][1000];
        int classID;
        try{
            File file = new File(InputParameters.path + cf.revision + "/clusters/cluster_vdb_30_5_allg_0.95_30"); //All Type

            if(file.exists()) {

                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file))); // All Type

                cfFile = fileRead(cf.revision);

                int flag = 0;
                for (int i = 0; i < cfFile.length; i++)
                    for (int j = 0; j < cfFile.length; j++)
                        if (cfFile[i][j] != null){
                            if (cf.filepath.equals(cfFile[i][j].filepath) && cf.startline == cfFile[i][j].startline && cf.endline == cfFile[i][j].endline) {
                                flag = 1;
                                classID = i;
                                return classID;
                            }
                            if (flag == 1)
                                break;
                        }
                        else
                            break;
            }

        } catch(Exception e){
            System.out.println("Error in method getClassID()." + e);
            e.printStackTrace();
        }
        return 0;

    }

    public int isClonePairBinary(CodeFragment cf1, CodeFragment cf2){
        int pair = 0;
        try{
            int classID1 = 0, classID2 = 0;

            classID1 = getClassID(cf1);

            classID2 = getClassID(cf2);

            if(classID1 == classID2){
                pair = 1;
            }
        } catch(Exception e){
            System.out.println("Error in method isClonePairBinary()." + e);
            e.printStackTrace();
        }
        return pair;

    }

    public CodeFragment getInstanceInNextRevision(CodeFragment cf) {
        try {
            CodeFragment instance = new CodeFragment();

            int crevision = cf.revision;
            int nrevision = crevision + 1;

            int nstartline = 999999999;
            int nendline = -1;

            int changed = 0;


            String cfilepath = InputParameters.path + crevision + "/" + cf.filepath;
            String nfilepath = InputParameters.path + nrevision + "/" + cf.filepath;

            File file = new File(nfilepath);
            if (!file.exists()) {
                return null;
            }

            String[][] filecompare = cc.compareFiles(cfilepath, nfilepath);

            for (int i = 0; filecompare[i][0] != null; i++) {
                String ln = filecompare[i][0].trim();
                if (ln.length() == 0) {
                    continue;
                }
                int line = Integer.parseInt(ln);
                if (line > cf.endline) {
                    break;
                }
                if (line == 509) {
                    int a = 10;
                }
                if (line >= cf.startline && line <= cf.endline) {
                    String nln = filecompare[i][2].trim(); // SHOWING NULL POINTER EXCEPTION HERE
                    if (nln.trim().length() > 0) {
                        int nline = Integer.parseInt(nln);
                        if (nstartline > nline) {
                            nstartline = nline;
                        }
                        if (nendline < nline) {
                            nendline = nline;
                        }
                    }
                    if (!filecompare[i][1].trim().equals(filecompare[i][3].trim())) {
                        if (filecompare[i][1].trim().length() > 0 || filecompare[i][3].trim().length() > 0) {
                            changed = 1;
                        }
                    }
                }
            }

            if (nendline == -1) {
                return null;
            }

            instance.revision = nrevision;
            instance.filepath = cf.filepath;
            instance.startline = nstartline;
            instance.endline = nendline;
            instance.changetype = cf.changetype;

            return instance;

        } catch (Exception e) {
            return null;
        }
    }

}
