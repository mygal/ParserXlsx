import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class ParserX {

    public static XSSFWorkbook book;

    public static void main(String[] args) throws IOException {
        try {
            File excel = new File("C://temp/SUM-dictionary-Experimental.xlsx");
            FileInputStream fis = new FileInputStream(excel);

            System.out.println("OPEN FILE");

            book = new XSSFWorkbook(fis);

            book.getSheetAt(0).getActiveCell().formatAsString();

            if (book.getSheet("HashMap") == null) {
                // Создаем новую вкладку для результатов
                book.createSheet("HashMap");
            }

            XSSFSheet sheetResult = book.getSheet("HashMap");

            //Comparator??
            Map<String, String> movingPartN = new HashMap<>();

            // Номара колонок для записи
            int rowNum = 0;

            //Побежали по страницам
            for (int sheetNum = 0; sheetNum < book.getNumberOfSheets(); sheetNum++) {

//            XSSFSheet sheetCurrent = book.getSheetAt(2);
                XSSFSheet sheetCurrent = book.getSheetAt(sheetNum);
                System.out.println("getSheetName: " + sheetCurrent.getSheetName() + " Num:" + sheetNum);
                Iterator<Row> itr = sheetCurrent.iterator();

                Cell CellPartN = null;
                Cell CellNomenclature = null;
                Cell cell = null;
                String str = "Позначення";

                //Начальный индекс = ничего не нашел.
                int numColumnPartN = -1;
                int numRowPartN = -1;
                //Нашел "Позначення", чтобы не гонять впустую цикл, флаг.
                Boolean ready = false;
                Row row = null;
                Iterator<Cell> cellIterator = null;

                // Iterating over Excel file in Java
                while (itr.hasNext()) {
                    row = itr.next();

                    // Iterating over each column of Excel file
                    cellIterator = row.cellIterator();

                    //ищем "Позначення"
                    while (!ready & cellIterator.hasNext()) {
                        cell = cellIterator.next();

                        //Находим адрес ячейки от которой будем дальше списывать
                        if (cell.toString().equals(str)) {
                            // Записываем номер колонки где ячейка "Позначення"
                            numColumnPartN = cell.getAddress().getColumn();
                            numRowPartN = cell.getRowIndex();
                            ready = true;
                            break;
                        }
                    }
                }// Финиш поиска по строкам

                Iterator<Row> itr2Rows = sheetCurrent.iterator();
                Row row2 = null;
                if (itr2Rows.hasNext()) {
                    row2 = itr2Rows.next();
                }

                Iterator<Cell> cellIterator2 = null;

                while (itr2Rows.hasNext()) {
                    row2 = itr2Rows.next();
                    cellIterator2 = row2.cellIterator();
                    //Нашли ячейку с "Позначення" и идем с итератором по ячейкам в строке
                    while (cellIterator2.hasNext()) {
                        //ячейка находится в колонке "Позначення" и ниже строки
                        if ((cell.getColumnIndex() == numColumnPartN)
                                & (cell.getRowIndex() > numRowPartN)) {
                            CellPartN = cell;
                            CellNomenclature = cellIterator2.next();

                            //Ячейка 1
                            //Убираем пробелы в ячейке
                            String str1 = CellPartN.toString().trim().replaceAll("\\s+", " ");
//                            String str1 = CellPartN.getStringCellValue().trim().replaceAll("\\s+", " ");
                            if ((str1 != "") & (str1.length() > 2)) {


                                /*char[] chars1 = str1.toCharArray();
                                int countCh1 = 0;

                                // Считаем кол-во пробелов
                                for (int i = 0; i < chars1.length; i++) {
                                    if (chars1[i] == ' ') {
                                        countCh1++;
                                    }
                                }
                                int pos[] = new int[countCh1];

                                int num = 0;
                                for (int i = 0; i < chars1.length; i++) {
                                    if (chars1[i] == ' ') {
                                        //записываем позицию пробела
                                        pos[num++] = i;
                                    }
                                }*/
                                str1 = getStringFromCell(str1, 1);
                            }

                            //Ячейка 2
                            String str2 = CellNomenclature.toString().
                                    trim().replaceAll("\\s+", " ");
//                            String str2 = CellNomenclature.getStringCellValue().
//                                    trim().replaceAll("\\s+", " ");


                            if ((str2 != "") & (str2.length() > 2)) {
                                /*char[] chars = str2.toCharArray();
                                int countCh1 = 0;

                                // Считаем кол-во пробелов
                                for (int i = 0; i < chars.length; i++) {
                                    if (chars[i] == ' ') {
                                        countCh1++;
                                    }
                                }
                                int pos[] = new int[countCh1];

                                int num = 0;
                                for (int i = 0; i < chars.length; i++) {
                                    if (chars[i] == ' ') {
                                        //записываем позицию пробела
                                        pos[num++] = i;
                                    }
                                }*/

                                //Если находится кирилица
                                // if (Character.UnicodeBlock.of(ch) == Character.UnicodeBlock.CYRILLIC)

                                str2 = getStringFromCell(str2, 2);

                            }
                            //записіваем мапу
                            movingPartN.put(str1, str2);
                        }
                        if (cellIterator2.hasNext()) {
                            cell = cellIterator2.next();
                        }
                    }
                }// Записали Мапу


                System.out.println("movingPartN.size(): " + movingPartN.size());

                Set<String> newRows = movingPartN.keySet();

                for (String key : newRows) {
                    Row rowOut = sheetResult.createRow(rowNum++);

                    Cell cellOut1 = rowOut.createCell(1);
                    cellOut1.setCellValue(key);

                    Cell cellOut2 = rowOut.createCell(2);
                    cellOut2.setCellValue(movingPartN.get(key));
                }
                rowNum++;

            }//цикл по листам книги

            // open an OutputStream to save written data into Excel file
            FileOutputStream os = new FileOutputStream(excel);
            book.write(os);
            System.out.println("Writing on Excel file Finished ...");
            // Close workbook, OutputStream and Excel file to prevent leak
            os.close();
            book.close();
            fis.close();
        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (IllegalArgumentException e) {
            book.removeSheetAt(3);
            System.out.println("Sheet 3 - REMOVED");
            throw e;
        }
    }

    // Сгенерированный метод для распределения данных в ячейке в зависимости от кол-ва пробелов
    //Если строка содержит одиночные пробелы типа "ГОСТ 8534 GOST 8534" ищем центральный пробел
    //и меняем его на перенос строки
    private static String getStringFromCell(String str, int mode) {

        char[] chars = str.toCharArray();
        int countCh = 0;

        // Считаем кол-во пробелов
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == ' ') {
                countCh++;
            }
        }
        int pos[] = new int[countCh];

        int num = 0;
        //Находим позиции пробелов
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == ' ') {
                //записываем позицию пробела
                pos[num++] = i;
            }
        }
        // расчет для первой ячейки
        if (mode == 1)
            switch (countCh) {
                case 0:
                    str = str.substring(0, chars.length / 2)
                            + "\n"
                            + str.substring(chars.length / 2);
                    break;
                case 1:
                    str = str.substring(0, pos[0])
                            + "\n"
                            + str.substring(pos[0] + 1);
                    break;
                case 2:
                    //Сравниваем половину длины строки и позицию первого пробела
                    if ((chars.length / 2) > pos[0] + 1) {
                        str = str.substring(0, pos[0])
                                + str.substring(pos[0] + 1, pos[1])
                                + "\n"
                                + str.substring(pos[1] + 1);
                    } else {
                        str = str.substring(0, pos[0])
                                + "\n"
                                + str.substring(pos[0] + 1, pos[1])
                                + str.substring(pos[1] + 1);
                    }
                    break;
                case 3:
                    str = str.substring(0, pos[1])
                            + "\n"
                            + str.substring(pos[1] + 1);
                    break;
                default:
                    str = str.substring(0, pos[countCh / 2])
                            + "\n"
                            + str.substring(pos[countCh / 2 + 1] + 1);
            }
        // расчет для второй ячейки
        if (mode == 2) {
            int posCyrilic = -1;
            int count = 0;
            //Находим позицию начала кирилицы
            for (char ch : chars) {
                count++;
                if (Character.UnicodeBlock.of(ch) == Character.UnicodeBlock.CYRILLIC) {
                    //записываем позицию пробела
                    posCyrilic = count;
                    break;
                }
            }
            if (posCyrilic != -1)
                str = str.substring(0, posCyrilic-2)
                        + "\n"
                        + str.substring(posCyrilic-1);
        }
        return str;
    }
}

