import java.util.*;
import java.util.List;
import java.util.ArrayList;
//Displays
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import java.awt.event.*;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.FlowLayout;
//readers
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.*;
//jar handling?
import java.net.*;
import java.nio.file.*;
import java.util.stream.*;
import java.nio.charset.StandardCharsets;
//link opening
import java.awt.Desktop;
/**
 * A Class designed to create a series of dialog prompts that help filter down UCLA 
 * clubs into a shortened list that is usable for club searching to students
 * 
 * @author      Maxwell Tsao
 * @footnote    assitance from Lucas Jeong
 * @version     0.1
 * @date        8/5/20
 */
public class CampushOnePointZero
{
    private static JFrame box;
    /**
     * runs the main prompts to do the club recommendations
     */
    public static void main(String[] args) throws URISyntaxException, IOException
    {
        //reads csv data
        boolean found;
        List<List<String>> content = new ArrayList<List<String>>();
        URI uri = CampushOnePointZero.class.getResource("student_organizations (2) (1).csv").toURI();
        Path myPath;
        if (uri.getScheme().equals("jar")) {
            FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
            found = true;
            myPath = fileSystem.getPath("student_organizations (2) (1).csv");
            content = readData(myPath);
        } else {
            try{
                content = readData("student_organizations (2) (1).csv");
                found = true;
            }
            catch(IOException e){
                found = false;
            }
        }

        //begins user prompting
        System.out.println("first: " + content.size());
        System.out.println("array: " + content.get(7));
        System.out.println("array size: " + content.get(7).size());
        System.out.println("array size: " + content.get(1000).size());

        if(found){
            String category = JOptionPane.showInputDialog("Category?");
            content = trim(content, category.toLowerCase(), 1);
            System.out.println("second: " + content.size());
            boolean flag = true;
            while(flag)
            {   
                String keyword = JOptionPane.showInputDialog("Description Keyword?(1 word please)");
                content = trim(content, keyword.toLowerCase(), 3);
                if(JOptionPane.showInputDialog("More?(y or n)").equals("n")){
                    flag = false;
                }
            }
            System.out.println("third: " + content.size());
            //shows results
            box = new JFrame("Thank you for using Campush 0.1");
            box.setSize(1500, 300);
            box.setLocation(750, 150);
            box.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            box.setVisible(true);
            fill(content);
        }
        else{
            System.out.println("Database not loaded");
            System.exit(0);
        }

    }

    /**
     * loads the final display
     */

    public static void fill(List<List<String>> last){
        JPanel home = new JPanel();
        box.setContentPane(home);
        home.setLayout(new FlowLayout());
        JLabel label = new JLabel("Your Recommended");
        home.add(label);
        JLabel label2 = new JLabel("Club amount: " + last.size() + "||||");
        home.add(label2);
        int n = last.size();
        if(last.size()>10)n = 11;
        for(int i = 1; i<n; i++){
            home.add(new JLabel("Club: "+ last.get(i).get(2) + " ||Website: " + last.get(i).get(7) + 
                    " ||Contact: " + last.get(i).get(5)));
            try {
                Desktop desktop = java.awt.Desktop.getDesktop();
                URI oURL = new URI(last.get(i).get(7));
                desktop.browse(oURL);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * removes all clubs that do not contain the proper keyword within a certain column
     */

    public static List<List<String>> trim(List<List<String>> current, String match, int column){
        int n = current.size();
        for(int i = n-1; i>1; i--){
            if(current.get(i).size()==1)System.out.println("whoops" + i);
            if(!current.get(i).get(column).toLowerCase().contains(match)){
                current.remove(i);
            }
        }
        return current;
    }

    /**
     * reads the csv file and returns it as a list of string lists
     */
    public static List<List<String>> readData(String filename) throws IOException {
        List<List<String>> res = new ArrayList<>();
        BufferedReader in = new BufferedReader(new FileReader(filename));
        String line;
        List<String> curList = new ArrayList<>();
        StringBuffer curString = new StringBuffer();
        boolean inQuotes = false, first = true;
        while((line = in.readLine()) != null) {
            if (inQuotes) {
                curString.append('\n');
            } else if(!first) {
                res.add(curList);
                curList = new ArrayList<>();
            }
            char[] str = line.toCharArray();
            int len = str.length;
            for (int i = 0; i < len; ++i) {
                if (str[i] == ',') {
                    if (inQuotes) {
                        curString.append(',');
                    } else {
                        curList.add(curString.toString());
                        curString = new StringBuffer();
                    }
                } else if (str[i] == '\"') {
                    inQuotes = !inQuotes;
                } else {
                    curString.append(str[i]);
                }
            }
            curList.add(curString.toString());
            first = false;
        }
        res.add(curList);
        in.close();
        return res;
    }

    /**
     * reads the csv file and returns it as a list of string lists
     */
    public static List<List<String>> readData(Path path) throws IOException {
        List<List<String>> res = new ArrayList<>();
        BufferedReader in = Files.newBufferedReader(path, StandardCharsets.UTF_8);
        String line;
        List<String> curList = new ArrayList<>();
        StringBuffer curString = new StringBuffer();
        boolean inQuotes = false, first = true;
        while((line = in.readLine()) != null) {
            if (inQuotes) {
                curString.append('\n');
            } else if(!first) {
                res.add(curList);
                curList = new ArrayList<>();
            }
            char[] str = line.toCharArray();
            int len = str.length;
            for (int i = 0; i < len; ++i) {
                if (str[i] == ',') {
                    if (inQuotes) {
                        curString.append(',');
                    } else {
                        curList.add(curString.toString());
                        curString = new StringBuffer();
                    }
                } else if (str[i] == '\"') {
                    inQuotes = !inQuotes;
                } else {
                    curString.append(str[i]);
                }
            }
            curList.add(curString.toString());
            first = false;
        }
        res.add(curList);
        in.close();
        return res;
    }
}