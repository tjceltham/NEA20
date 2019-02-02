package com.company;

import java.util.Random;

class Main {
    static char SOIL = '.';
    static char SEED = 'S';
    static char PLANT = 'P';
    static char ROCKS = 'X';
    static int FIELDLENGTH = 20;
    static int FIELDWIDTH = 35;

    static int GetHowLongToRun() {
        int Years = 0;
        Console.println("Welcome to the Plant Growing Simulation");
        Console.println();
        Console.println("You can step through the simulation a year at a time");
        Console.println("or run the simulation for 0 to 5 years");
        Console.println("How many years do you want the simulation to run?");
        Years = Console.readInteger("Enter a number between 0 and 5, or -1 for stepping mode: ");
        return Years;
    }

    static void CreateNewField(char[][] Field) {
        int Row = 0;
        int Column = 0;
        for (Row = 0; Row < FIELDLENGTH; Row++) {
            for (Column = 0; Column < FIELDWIDTH; Column++) {
                Field[Row][Column] = SOIL;
            }
        }
        Row = FIELDLENGTH / 2;
        Column = FIELDWIDTH / 2;
        Field[Row][Column] = SEED;
    }

    static void ReadFile(char[][] Field) {
        String FileName = "";
        String FieldRow = "";
        Console.print("Enter file name: ");
        FileName = Console.readLine();
        try {
            for (int Row = 0; Row < FIELDLENGTH; Row++) {
                for (int Column = 0; Column < FIELDWIDTH; Column++) {
                    Field[Row][Column] = SOIL;
                }
            }
            AQAReadTextFile2017 FileHandle = new AQAReadTextFile2017(FileName);
            for (int Row = 0; Row < FIELDLENGTH; Row++) {
                FieldRow = FileHandle.readLine();
                for (int Column = 0; Column < FIELDWIDTH; Column++) {
                    Field[Row][Column] = FieldRow.charAt(Column);
                }
            }
            FileHandle.closeFile();
        } catch (Exception e) {
            CreateNewField(Field);
        }
    }

    static void InitialiseField(char[][] Field) {
        String Response = "";
        Console.print("Do you want to load a file with seed positions? (Y/N): ");
        Response = Console.readLine();
        if (Response.equals("Y")) {
            ReadFile(Field);
        } else {
            CreateNewField(Field);
        }
    }

    static void Display(char[][] Field, String Season, int Year) {
        Console.println("Season: " + Season + "  Year number: " + Year);
        for (int Row = 0; Row < FIELDLENGTH; Row++) {
            for (int Column = 0; Column < FIELDWIDTH; Column++) {
                Console.print(Field[Row][Column]);
            }
            Console.println("|" + String.format("%3d", Row));
        }
    }

    static void CountPlants(char[][] Field) {
        int NumberOfPlants = 0;
        for (int Row = 0; Row < FIELDLENGTH; Row++) {
            for (int Column = 0; Column < FIELDWIDTH; Column++) {
                if (Field[Row][Column] == PLANT) {
                    NumberOfPlants++;
                }
            }
        }
        if (NumberOfPlants == 1) {
            Console.println("There is 1 plant growing");
        } else {
            Console.println("There are " + NumberOfPlants + " plants growing");
        }
    }

    static void SimulateSpring(char[][] Field) {
        int PlantCount = 0;
        Boolean Frost = false;
        for (int Row = 0; Row < FIELDLENGTH; Row++) {
            for (int Column = 0; Column < FIELDWIDTH; Column++) {
                if (Field[Row][Column] == SEED) {
                    Field[Row][Column] = PLANT;
                }
            }
        }
        CountPlants(Field);
        Random RandomInt = new Random();
        if (RandomInt.nextInt(2) == 1) {
            Frost = true;
        } else {
            Frost = false;
        }
        if (Frost) {
            PlantCount = 0;
            for (int Row = 0; Row < FIELDLENGTH; Row++) {
                for (int Column = 0; Column < FIELDWIDTH; Column++) {
                    if (Field[Row][Column] == PLANT) {
                        PlantCount++;
                        if (PlantCount % 3 == 0) {
                            Field[Row][Column] = SOIL;
                        }
                    }
                }
            }
            Console.println("There has been a frost");
            CountPlants(Field);
        }
    }

    static void SimulateSummer(char[][] Field) {
        Random RandomInt = new Random();
        int RainFall = RandomInt.nextInt(3);
        int PlantCount = 0;
        if (RainFall == 0) {
            PlantCount = 0;
            for (int Row = 0; Row < FIELDLENGTH; Row++) {
                for (int Column = 0; Column < FIELDWIDTH; Column++) {
                    if (Field[Row][Column] == PLANT) {
                        PlantCount++;
                        if (PlantCount % 2 == 0) {
                            Field[Row][Column] = SOIL;
                        }
                    }
                }
            }
            Console.println("There has been a drought");
            CountPlants(Field);
        }
    }

    static void SeedLands(char[][] Field, int Row, int Column) {
        if (Row >= 0 && Row < FIELDLENGTH && Column >= 0 && Column < FIELDWIDTH) {
            if (Field[Row][Column] == SOIL) {
                Field[Row][Column] = SEED;
            }
        }
    }

    static void SimulateAutumn(char[][] Field) {
        for (int Row = 0; Row < FIELDLENGTH; Row++) {
            for (int Column = 0; Column < FIELDWIDTH; Column++) {
                if (Field[Row][Column] == PLANT) {
                    SeedLands(Field, Row - 1, Column - 1);
                    SeedLands(Field, Row - 1, Column);
                    SeedLands(Field, Row - 1, Column + 1);
                    SeedLands(Field, Row, Column - 1);
                    SeedLands(Field, Row, Column + 1);
                    SeedLands(Field, Row + 1, Column - 1);
                    SeedLands(Field, Row + 1, Column);
                    SeedLands(Field, Row + 1, Column + 1);
                }
            }
        }
    }

    static void SimulateWinter(char[][] Field) {
        for (int Row = 0; Row < FIELDLENGTH; Row++) {
            for (int Column = 0; Column < FIELDWIDTH; Column++) {
                if (Field[Row][Column] == PLANT) {
                    Field[Row][Column] = SOIL;
                }
            }
        }
    }

    static void SimulateOneYear(char[][] Field, int Year) {
        SimulateSpring(Field);
        Display(Field, "spring", Year);
        SimulateSummer(Field);
        Display(Field, "summer", Year);
        SimulateAutumn(Field);
        Display(Field, "autumn", Year);
        SimulateWinter(Field);
        Display(Field, "winter", Year);
    }

    private static void Simulation() {
        int YearsToRun;
        char[][] Field = new char[FIELDLENGTH][FIELDWIDTH];
        Boolean Continuing;
        int Year;
        String Response;
        YearsToRun = GetHowLongToRun();
        if (YearsToRun != 0) {
            InitialiseField(Field);
            if (YearsToRun >= 1) {
                for (Year = 1; Year <= YearsToRun; Year++) {
                    SimulateOneYear(Field, Year);
                }
            } else {
                Continuing = true;
                Year = 0;
                while (Continuing) {
                    Year++;
                    SimulateOneYear(Field, Year);
                    Console.print("Press Enter to run simulation for another Year, Input X to stop: ");
                    Response = Console.readLine();
                    if (Response.equals("x") || Response.equals("X")) {
                        Continuing = false;
                    }
                }
            }
            Console.println("End of Simulation");
        }
        Console.readLine();
    }

    public static void main(String[] args) {
        Simulation();
    }
}