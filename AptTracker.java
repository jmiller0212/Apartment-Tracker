// Jarod Miller

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;

public class AptTracker {

    public static ArrayList<Apt> loadApartments(ArrayList<Apt> apartments) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("apartments.txt"));
            String line = br.readLine();
            line = br.readLine();
            String delim = "[:]";
            String[] tokens;
            while(line != null) {
                tokens = line.split(delim);
                Apt a = new Apt(tokens[0],tokens[1],tokens[2],tokens[3],
                    Integer.parseInt(String.valueOf(tokens[4])),
                    Integer.parseInt(String.valueOf(tokens[5])));
                apartments.add(a);
                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return apartments;
    }

    public static void displayOptions() {
        System.out.println("(0) Exit program");
        System.out.println("(1) Add an apartment");
        System.out.println("(2) Update an apartment");
        System.out.println("(3) Remove a specific apartment from consideration");
        System.out.println("(4) Retrieve the lowest rent apartment");
        System.out.println("(5) Retrieve the highest square footage apartment");
        System.out.println("(6) Retrieve the lowest rent apartment by city");
        System.out.println("(7) Retrieve the highest square footage apartment by city");
    }

    public static void printApartments(ArrayList<Apt> apartments) {
        for(int i = 0; i < apartments.size(); i++) {
            Apt a = apartments.get(i);
            System.out.print(a.getStreetAddy());
            System.out.print(" "+a.getAptNumber());
            System.out.print(" "+a.getCity());
            System.out.print(" "+a.getZipCode());
            System.out.print(" "+a.getRent());
            System.out.println(" "+a.getSqFootage());
        }
        System.out.println();
    }

    public static void printApartment(Apt a) {
        System.out.print(a.getStreetAddy());
        System.out.print(" "+a.getAptNumber());
        System.out.print(" "+a.getCity());
        System.out.print(" "+a.getZipCode());
        System.out.print(" "+a.getRent());
        System.out.println(" "+a.getSqFootage());
    }
    public static void main(String[] args) {
        ArrayList<Apt> apartments = new ArrayList<Apt>();
        apartments = loadApartments(apartments);
        MinRentPQ<Integer> minpq  = new MinRentPQ<>(apartments.size()*2);
        MaxSqFtPQ<Integer> maxpq = new MaxSqFtPQ<>(apartments.size()*2);
        HashMap<String, Integer> indexhm = new HashMap<>();

        HashMap<String, Integer> cityindexhm = new HashMap<>();
        HashMap<String, Integer> doublecityindexhm = new HashMap<>();
        HashMap<Integer, ArrayList<Apt>> citypqsizehm = new HashMap<>();
        ArrayList<MinRentPQ<Integer>> cityrental = new ArrayList<>();
        ArrayList<MaxSqFtPQ<Integer>> citysqftal = new ArrayList<>();

        for(int i = 0; i < apartments.size(); i++) {
            Apt a = apartments.get(i);
            String hashkey = a.getStreetAddy()+a.getAptNumber()+a.getZipCode();
            minpq.insert(i, a.getRent());
            maxpq.insert(i, a.getSqFootage());
            indexhm.put(hashkey, i);
            if(!cityindexhm.containsKey(a.getCity())) {
                MinRentPQ<Integer> mincitypq  = new MinRentPQ<>(apartments.size());
                MaxSqFtPQ<Integer> maxcitypq  = new MaxSqFtPQ<>(apartments.size());
                mincitypq.insert(0, a.getRent());
                maxcitypq.insert(0, a.getSqFootage());
                int alindex = cityrental.size();
                cityrental.add(alindex, mincitypq);
                citysqftal.add(alindex, maxcitypq);
                cityindexhm.put(a.getCity(), alindex);
                doublecityindexhm.put(hashkey, 0);
                ArrayList<Apt> cityapts = new ArrayList<Apt>();
                cityapts.add(a);
                citypqsizehm.put(alindex, cityapts);
            }
            else {
                int alindex = cityindexhm.get(a.getCity());
                MinRentPQ<Integer> mincitypq = cityrental.get(alindex);
                MaxSqFtPQ<Integer> maxcitypq  = citysqftal.get(alindex);
                int nextpqindex = mincitypq.size();
                mincitypq.insert(nextpqindex, a.getRent());
                maxcitypq.insert(nextpqindex, a.getSqFootage());
                doublecityindexhm.put(hashkey, nextpqindex);
                ArrayList<Apt> cityapts = citypqsizehm.get(alindex);
                cityapts.add(a);
                citypqsizehm.put(alindex, cityapts);
            }
        }

        while(true) {
            displayOptions();
            System.out.print("Choose option: ");
            Scanner input = new Scanner(System.in);
            int option = Integer.parseInt(input.nextLine());
            System.out.println();
            String street;
            String aptnum;
            String city;
            String zip;
            int rent;
            int sqft;
            if(option == 0) {
                input.close();
                break;
            }
            if(option == 1) { // add an apartment
                System.out.println("What is the street address? ");
                street = input.nextLine();
                System.out.println("What is the apartment number? ");
                aptnum = input.nextLine();
                System.out.println("What is the city? ");
                city = input.nextLine();
                System.out.println("What is the zip code? ");
                zip = input.nextLine();
                System.out.println("What is the monthly rent? ");
                rent = Integer.parseInt(input.nextLine());
                System.out.println("What is the square footage? ");
                sqft = Integer.parseInt(input.nextLine());
                Apt a = new Apt(street, aptnum, city, zip, rent, sqft);
                apartments.add(a);
                String hashkey = street+aptnum+zip;
                indexhm.put(hashkey, apartments.size()-1);
                minpq.insert(apartments.size()-1, a.getRent());
                maxpq.insert(apartments.size()-1, a.getSqFootage());


                if(!cityindexhm.containsKey(a.getCity())) {
                    MinRentPQ<Integer> mincitypq  = new MinRentPQ<>(apartments.size());
                    MaxSqFtPQ<Integer> maxcitypq  = new MaxSqFtPQ<>(apartments.size());
                    mincitypq.insert(0, a.getRent());
                    maxcitypq.insert(0, a.getSqFootage());
                    int alindex = cityrental.size();
                    cityrental.add(alindex, mincitypq);
                    cityindexhm.put(a.getCity(), alindex);
                    doublecityindexhm.put(hashkey, 0);
                    ArrayList<Apt> cityapts = new ArrayList<Apt>();
                    cityapts.add(a);
                    citypqsizehm.put(alindex, cityapts);
                }
                else {
                    int alindex = cityindexhm.get(a.getCity());
                    MinRentPQ<Integer> mincitypq = cityrental.get(alindex);
                    MaxSqFtPQ<Integer> maxcitypq  = citysqftal.get(alindex);
                    int nextpqindex = mincitypq.size();
                    mincitypq.insert(nextpqindex, a.getRent());
                    maxcitypq.insert(nextpqindex, a.getSqFootage());
                    ArrayList<Apt> cityapts = citypqsizehm.get(alindex);
                    cityapts.add(a);
                    citypqsizehm.put(alindex, cityapts);
                    doublecityindexhm.put(hashkey, nextpqindex);
                }
            }
            if(option == 2) { // update an apartment
                System.out.println("What is the street address? ");
                street = input.nextLine();
                System.out.println("What is the apartment number? ");
                aptnum = input.nextLine();
                System.out.println("What is the zip code? ");
                zip = input.nextLine();
                System.out.println("Would you like to update the rent for this apt? (yes/no)");
                String answer = input.nextLine();
                if(answer.equals("yes")) {
                    System.out.println("What is the new monthly rent?");
                    int newRent = Integer.parseInt(input.nextLine());
                    String hashkey = street+aptnum+zip;
                    int alindex = indexhm.get(hashkey);
                    Apt a = apartments.get(alindex);
                    a.setRent(newRent);
                    apartments.set(alindex, a);
                    minpq.changeKey(alindex, a.getRent());

                    alindex = cityindexhm.get(a.getCity());
                    MinRentPQ<Integer> mincitypq = cityrental.get(alindex);
                    int pqindex = doublecityindexhm.get(hashkey);
                    mincitypq.changeKey(pqindex, a.getRent());
                    ArrayList<Apt> cityapts = citypqsizehm.get(alindex);
                    cityapts.set(pqindex, a);
                }
            }
            if(option == 3) { // remove a specific apartment
                System.out.println("What is the street address? ");
                street = input.nextLine();
                System.out.println("What is the apartment number? ");
                aptnum = input.nextLine();
                System.out.println("What is the zip code? ");
                zip = input.nextLine();
                String hashkey = street+aptnum+zip;
                int alindex = indexhm.get(hashkey);

                Apt a = apartments.get(alindex);
                int cityalindex = cityindexhm.get(a.getCity());
                System.out.println(cityalindex);
                MinRentPQ<Integer> mincitypq = cityrental.get(cityalindex);
                MaxSqFtPQ<Integer> maxcitypq = citysqftal.get(cityalindex);
                int theindex = doublecityindexhm.get(hashkey);
                mincitypq.delete(theindex);
                maxcitypq.delete(theindex);

                ArrayList<Apt> cityapts = citypqsizehm.get(cityalindex);
                cityapts.set(theindex, null);
                citypqsizehm.put(cityalindex, cityapts);

                apartments.set(alindex, null);
                minpq.delete(alindex);
                maxpq.delete(alindex);
            }
            if(option == 4) { // retrieve the lowest rent apartment
                int alindex = minpq.minIndex();
                printApartment(apartments.get(alindex));
            }
            if(option == 5) {
                int alindex = maxpq.maxIndex();
                printApartment(apartments.get(alindex));
            }
            if(option == 6) {
                System.out.println("What city would you like to see? ");
                city = input.nextLine();
                int alindex = cityindexhm.get(city);
                MinRentPQ<Integer> mincitypq = cityrental.get(alindex);
                ArrayList<Apt> cityapts = citypqsizehm.get(alindex);
                int i = mincitypq.minIndex();
                printApartment(cityapts.get(i));
            }
            if(option == 7) {
                System.out.println("What city would you like to see? ");
                city = input.nextLine();
                int alindex = cityindexhm.get(city);
                MaxSqFtPQ<Integer> maxcitypq = citysqftal.get(alindex);
                ArrayList<Apt> cityapts = citypqsizehm.get(alindex);
                int i = maxcitypq.maxIndex();
                printApartment(cityapts.get(i));
            }
        }
    }
}