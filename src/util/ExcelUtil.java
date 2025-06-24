package util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {
    // Database configuration
    private static final String DB_FOLDER = "database";
    private static final String DB_FILENAME = "users.xlsx";
    private static final String DB_PATH = DB_FOLDER + File.separator + DB_FILENAME;

    /**
     * Read users from the default users.xlsx file
     */
    public static Map<String, String> readUsers() {
        // Keep your existing implementation to ensure backward compatibility
        Map<String, String> users = new HashMap<>();
        
        // Verify database file exists
        File dbFile = new File(DB_PATH);
        if (!dbFile.exists()) {
            System.err.println("Database file not found at: " + dbFile.getAbsolutePath());
            System.err.println("Please ensure the file exists in the 'database' folder");
            return users; // Return empty map if file doesn't exist
        }

        try (FileInputStream fis = new FileInputStream(dbFile);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
            
            XSSFSheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                Cell userCell = row.getCell(0);
                Cell passCell = row.getCell(1);

                String username = getCellValueAsString(userCell);
                String password = getCellValueAsString(passCell);

                if (!username.isEmpty() && !password.isEmpty()) {
                    users.put(username, password);
                }
            }

        } catch (Exception e) {
            System.err.println("Error reading database file:");
            e.printStackTrace();
        }
        
        return users;
    }

    /**
     * Read users from a specified Excel file
     * @param filePath Path to the Excel file
     * @return Map of username to password
     */
    public static Map<String, String> readUsers(String filePath) {
        Map<String, String> users = new HashMap<>();
        
        // Verify file exists
        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("File not found at: " + file.getAbsolutePath());
            return users; // Return empty map if file doesn't exist
        }

        try (FileInputStream fis = new FileInputStream(file);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
            
            XSSFSheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                Cell userCell = row.getCell(0);
                Cell passCell = row.getCell(1);

                String username = getCellValueAsString(userCell);
                String password = getCellValueAsString(passCell);

                if (!username.isEmpty() && !password.isEmpty()) {
                    users.put(username, password);
                }
            }

        } catch (Exception e) {
            System.err.println("Error reading Excel file: " + filePath);
            e.printStackTrace();
        }
        
        return users;
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    // Optional: Method to verify database setup
    public static boolean isDatabaseAvailable() {
        File dbFile = new File(DB_PATH);
        return dbFile.exists();
    }
    
    /**
     * Check if a specific Excel file exists
     * @param filePath Path to the Excel file
     * @return true if file exists, false otherwise
     */
    public static boolean isFileAvailable(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }
    
    /**
     * Read generic data from any Excel file
     * @param filePath Path to the Excel file
     * @param sheetIndex Index of the sheet to read (0-based)
     * @return List of rows where each row is a list of cell values as strings
     */
    public static List<List<String>> readExcelData(String filePath, int sheetIndex) {
        List<List<String>> data = new ArrayList<>();
        
        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("File not found at: " + file.getAbsolutePath());
            return data;
        }
        
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            
            for (Row row : sheet) {
                List<String> rowData = new ArrayList<>();
                boolean hasData = false;
                
                for (int i = 0; i < row.getLastCellNum(); i++) {
                    Cell cell = row.getCell(i);
                    String value = getCellValueAsString(cell);
                    rowData.add(value);
                    if (!value.isEmpty()) {
                        hasData = true;
                    }
                }
                
                if (hasData) {
                    data.add(rowData);
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error reading Excel file: " + filePath);
            e.printStackTrace();
        }
        
        return data;
    }
}