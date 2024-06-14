
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Main  {
    public static void main(String[] args) {
        int dimension = 10; // Dimension of our tree
        List<KdTree.Node> data = new ArrayList<KdTree.Node>(); //list is just a resizable array
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        String line;
        String splitBy = ",";
        int counter = 0;
        int k ;
        int N;
        Long timea;
        Long timeb;
        long AverageT = 0;
        double[] input = new double[10];

        try {

            BufferedReader br = new BufferedReader(new FileReader("src/data.csv")); //create an object that can read

            br.readLine(); //skip first line
            while ((line = br.readLine()) != null) { // Loop to read all the lines


                String[] reader = line.split(splitBy); // Separate the line by the comma and save it into the reader array
                int Id = Integer.parseInt(reader[0]); // Convert string into int and save it
                for (int i = 2; i < 12; i++) {
                    input[i - 2] = Double.parseDouble(reader[i]); //Get the 10 attributes of the node
                }
                KdTree.Node node = new KdTree.Node(input, Id, reader[1]);
                data.add(counter, node);
                counter++;
            }

            List<KdTree.Node> Train_data = new ArrayList<KdTree.Node>();
            List<KdTree.Node> Test_data = new ArrayList<KdTree.Node>();

            System.out.println("What is the data size ?");
            N = Integer.parseInt(scanner.nextLine());
            System.out.println("What is the K Factor ? (1-7)");
            k = Integer.parseInt(scanner.nextLine());
            if (N > data.size()) throw new RuntimeException(" Use your brain");
            System.out.println("The training data is " + N * 4 / 5 + " and the testing data is " + N / 5 + " K: " + k);
            // Assuming that is the data is already shuffled because it is sorted with the Id which has no link for our use
            for (int i = 0; i < N * 4 / 5; i++) {
                int r = random.nextInt(N - i);
                Train_data.add(data.get(r));  //take the input from data list and put it in train  also I added N - i just so we assure  a hit
                data.remove(r); //Shift the list so even if we get the same random number it wont give the same node
            }
            for (int i = 0; i < N / 5; i++) {
                int r = random.nextInt(N / 5 - i);
                Test_data.add(data.get(r));
                data.remove(r);
            }

            //Make the actual tree
            KdTree Tree_ = new KdTree(dimension, Train_data);
            float Acc = 0;
            String a;
            for (KdTree.Node testDatum : Test_data) {
                //System.out.println("Input [ Id : " + Test_data.get(i).getId() + " Result :  " + Test_data.get(i).getDiagnosis() + " { " + Train_data.get(i).toString() + "}]");
                timea = System.nanoTime();
                a = Tree_.FindNearest(testDatum, k).getDiagnosis();
                timeb = System.nanoTime();
                AverageT = timeb - timea + AverageT;
                //System.out.println(a);
                if (a.equals(testDatum.getDiagnosis())) Acc++;

            }
            System.out.println(" The Average time is  " + (AverageT/Test_data.size()) + " Nano second" );
            System.out.println(" The Total time is  " + (AverageT) + " Nano second" );
            System.out.println(" The accuracy is " + (Acc/Test_data.size()) *100 + "%" );




        } catch (IOException | RuntimeException |IllegalAccessException  e) {
            e.printStackTrace();
        }

    }
}
