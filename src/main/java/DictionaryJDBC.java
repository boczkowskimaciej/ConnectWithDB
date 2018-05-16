import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DictionaryJDBC {

    // connect with DB using JDBC- training
    // TO DO: 1. input word from user, 2.Check is word exist in polish language
    // 3. Add or not add word on local DB, 4. If add, check is word exist in local DB

    private void run(){
        String inputWord = inputWord();

        if (isWordExist(inputWord)) {
            System.out.println("Such a word exist in polish language");
            String answerYesNo = inputYesNo();
            manageAnswer(inputWord,answerYesNo);
        }
        else {
            System.out.println("Such a word not exist in polish language");
            String answerYesNo = inputYesNo();
            manageAnswer(inputWord,answerYesNo);
        }
    }

    private void manageAnswer(String inputWord, String answerYesNo){

            if (answerYesNo.equals("Y") || answerYesNo.equals("y")) {
                if (checkIsExistInDbUsingJDBC(inputWord)) {
                    System.out.println("This word is already in database");
                } else {
                    addInDbUsingJDBC(inputWord);
                    System.out.println("Added word in database");
                }
            }
            else {
                System.out.println("No word added");
            }
        }

    private void addInDbUsingJDBC(String inputWord){
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/dictionary","postgres","postgres");
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO words(word) values "+ "('"+ inputWord +"');";
            stmt.execute(sql);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    private boolean checkIsExistInDbUsingJDBC(String inputWord){
        String word;
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/dictionary","postgres","postgres");
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM words";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()){
                word = rs.getString("word");
                if (inputWord.equals(word)){
                    return true;
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String inputWord(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input polish word: ");
        return scanner.next();
    }

    private String inputYesNo(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want add this word to your local database ? [Y/N] ");
        return scanner.next();
    }

    private boolean isWordExist(String isWordExist){
        List<String> listWords = new ArrayList<String>();
        try {
            FileReader fileReader = new FileReader("src/main/resources/slowa.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String oneLine = null;
            try {
                while ((oneLine = bufferedReader.readLine()) != null){
                    listWords.add(oneLine);
            }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (String listWord : listWords) {
            if (isWordExist.equals(listWord)) {
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        DictionaryJDBC word = new DictionaryJDBC();
        word.run();
    }
}
