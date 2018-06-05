public class rrrrr {
    public static void main(String[] args) {

        String str1 = "459М.17.033 459М. 17.033";

        //Если строка содержит одиночные пробелы типа "ГОСТ 8534 GOST 8534" ищем центральный пробел
        //и меняем его на перенос строки
        char[] chars1 = str1.toCharArray();
        int countCh1 = 0;

        // Считаем кол-во пробелов
        for (int i = 0; i < chars1.length; i++) {
            if (chars1[i] == ' ') {
                countCh1++;
            }
        }
        int pos[] = new int[countCh1];

        int count = 0;
        for (int i = 0; i < chars1.length; i++) {
            if (chars1[i] == ' ') {
                //записываем позицию пробела
                pos[count++] = i;
            }
        }
//        System.out.println("pos[]:" + Arrays.toString(pos));

//        System.out.println("Before str = " + str1);

        // 459М.17.033 459М. 17.033
        //Сравниваем половину длины строки и позицию первого пробела
//        System.out.println("chars1.length / 2  = " + chars1.length / 2 );
        if ((chars1.length / 2 ) > pos[0]+1) {
            str1 = str1.substring(0, pos[0])
                    + str1.substring(pos[0]+1, pos[1])
                    + "\n"
                    + str1.substring(pos[1] + 1);
//            System.out.println("CASE 1");
        } else {
            str1 = str1.substring(0, pos[0])
                    + "\n"
                    + str1.substring(pos[0], pos[1])
                    + str1.substring(pos[1]+1);
//            System.out.println("CASE 2");
        }

//        System.out.println("After  str = " + str1);

//-----------------------------------------------------------

        String str2 = "Cotter pin 2,5x16.016                        Шплінт 2,5x16.016";
        str2 = str2.trim().replaceAll("\\s+", " ");

        char[] chars = str2.toCharArray();
        int posCyrilic = -1;
        int count2 = 0;
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
            str2 = str2.substring(0, posCyrilic-2)
                    + "\n"
                    + str2.substring(posCyrilic);

        System.out.println("posCyrilic: " + posCyrilic);
        System.out.println("str2: " + str2);

    }




}
