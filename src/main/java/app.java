// importowane klasy
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;


import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Chromaticity;

import javax.print.attribute.standard.PageRanges;
import javax.print.attribute.standard.Sides;
import javax.swing.*;
import java.awt.*;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.*;

import java.util.ArrayList;

import java.util.List;

// wykonywalna klasa
public class app {



    public static void main(String[] args) throws IOException, DocumentException, PrintException, PrinterException {
        PdfStamper nowyPdf=null;
        int liczbaStron=0;
        int polozenieZnakuWodnego=0;
        JFrame frame = new JFrame();
        JProgressBar progressBar=new JProgressBar(JProgressBar.HORIZONTAL);
        progressBar.setBackground(Color.BLACK);
        progressBar.setForeground(Color.GREEN);
        progressBar.setString("Oczekiwanie na wydruk...");
        progressBar.setStringPainted(true);
        progressBar.setIndeterminate(true);
        progressBar.setSize(300,80);
        frame.add(progressBar);
        frame.setBounds(250,250,300,80);
        frame.setVisible(true);

        // wczytywanie oryginalnego dokumentu Pdf, args [0] oznacza tekstowy parametr nr 1. Jest to w naszym przypadku
        // sciezka do oryginalnego pliku Pdf. Ten parametr podajemy w kodzie VBA
        PdfReader oryginalnyPdf = new PdfReader(args[0]);
        //PdfReader oryginalnyPdf = new PdfReader("C:\\Users\\mike\\Documents\\testMB.pdf");
        // tworzenie kopii oryginalnego Pdf, args [1] to scieżka gdzie ma się ten plik znajdować
        FileOutputStream linkNowegoPdf = new FileOutputStream(args[1]);

        String [] arr = args[3].split(",");
        //String [] arr = "1,3,9,15,100".split(",");
        // FileOutputStream linkNowegoPdf=new FileOutputStream("C:\\Users\\mike\\Documents\\xx2.pdf");
        // PdfStamper nowyPdf = konwersjaPdf(oryginalnyPdf,args[1],args[3]);
        if (!arr[0].equals("caly dokument")){


            //nowyPdf = konwersjaPdf(oryginalnyPdf,args[1],arr);

            oryginalnyPdf.selectPages(konwersjaArgumentowZVBA(arr,oryginalnyPdf.getNumberOfPages()));

            //nowyPdf = konwersjaPdf(oryginalnyPdf,"C:\\Users\\mike\\Documents\\xx2.pdf",arr);


        }

        //fileOutputStream=new FileOutputStream(args[1]);



        nowyPdf=new PdfStamper(oryginalnyPdf,linkNowegoPdf);



        // ustawiamy jaki rodzaj czcionki i wielkosc ma miec znak wodny
        //Font czcionka = new Font(Font.FontFamily.TIMES_ROMAN,12);
        BaseFont font=BaseFont.createFont(BaseFont.TIMES_ROMAN,BaseFont.CP1250,BaseFont.EMBEDDED);
        Font czcionka=new Font(font,10,Font.NORMAL);
        // budowanie znaku wodnego, args [2] określa jaką treść ma mieć znak wodny. Parametr podawany w VBA
        //InputStreamReader inputStreamReader=new InputStreamReader(new ByteArrayInputStream(args[2].getBytes()), Charset.forName("UTF-8"));
        //BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
        //String tekstZArgumentu = bufferedReader.readLine();
        Phrase znakWodny = new Phrase(args[2],czcionka);
        //Phrase znakWodny = new Phrase("TEST",czcionka);
        //Phrase znakWodny = new Phrase("TEST",czcionka);
        // pętla, która przechodzi przez wszystkie strony dokumentu i umieszcza znak wodny
        for (int i = 1; i <=oryginalnyPdf.getNumberOfPages() ; i++) {
            // za pomocą tego obiektu odwołujemy się do pojedyńczej strony pdf
            PdfContentByte stronaPdf = nowyPdf.getOverContent(i);
            Rectangle rectangle=oryginalnyPdf.getPageSizeWithRotation(i);
            float szerokoscStronyZRotacja=rectangle.getWidth();
            float szerokoscStrony=nowyPdf.getImportedPage(oryginalnyPdf,i).getWidth();

            if(args.length>4){

                Image image=Image.getInstance(args[4]);
                //Image image=Image.getInstance("C:\\Users\\mike\\Documents\\etykieta.png");

                image.scaleAbsolute(330,90);


                if(nowyPdf.getImportedPage(oryginalnyPdf,i).getWidth()>=nowyPdf.getImportedPage(oryginalnyPdf,i).getHeight()){
                    if(szerokoscStronyZRotacja==szerokoscStrony) {
                        image.setAbsolutePosition(180, 453);
                        polozenieZnakuWodnego=400;}
                    else{


                        oryginalnyPdf.getPageN(i).put(PdfName.ROTATE,new PdfNumber(180));
                        image.setAbsolutePosition(180, 453);
                        polozenieZnakuWodnego=400;

                    }


                }else{
                    if(szerokoscStronyZRotacja==szerokoscStrony){
                        image.setAbsolutePosition(79, 683);
                        polozenieZnakuWodnego=270;}
                    else{
                        oryginalnyPdf.getPageN(i).put(PdfName.ROTATE,new PdfNumber(180));
                        image.setAbsolutePosition(79, 683);
                        polozenieZnakuWodnego=270;

                    }

                }

                nowyPdf.getOverContent(i).addImage(image);
            }
            if(args.length==4){
                if(nowyPdf.getImportedPage(oryginalnyPdf,i).getWidth()>=nowyPdf.getImportedPage(oryginalnyPdf,i).getHeight()){
                    if(szerokoscStronyZRotacja==szerokoscStrony) {

                        polozenieZnakuWodnego=400;}



                }else{
                    if(szerokoscStronyZRotacja==szerokoscStrony){

                        polozenieZnakuWodnego=270;}


                }
            }


//            image.scaleAbsolute(330,90);
//                        if(arr[0].equals("caly dokument")) {
//                            if (nowyPdf.getImportedPage(oryginalnyPdf, i).getWidth() > 700) {
//                                image.setAbsolutePosition(170, 450);
//                                polozenieZnakuWodnego=400;
//                            } else {
//                                image.setAbsolutePosition(75, 675);
//                                polozenieZnakuWodnego=150;
//                            }
//                        }else{
//
//                            if(nowyPdf.getImportedPage(oryginalnyPdf, Integer.parseInt(arr[i-1])).getWidth()>700){
//
//                                image.setAbsolutePosition(170, 450);
//                                polozenieZnakuWodnego=400;
//
//                            }else{
//                                image.setAbsolutePosition(75, 675);
//                                polozenieZnakuWodnego=150;
//
//                            }
//
//
//
//
//                        }

            //Image image=Image.getInstance(args[4]);



            //}


            // za[isujemy jej ustawienia poczatkowe
            stronaPdf.saveState();
            // tworzymy obiekt za pomoca ktorego mozna zmieniac ustawienia wizualne
            PdfGState ustawienieWizualneStronyPdf = new PdfGState();
            // ustawiamy transparentność przyszłego znaku na 50 %
            ustawienieWizualneStronyPdf.setFillOpacity(0.5f);
            stronaPdf.setGState(ustawienieWizualneStronyPdf);
            // łączymy utworzony wcześniej znak wodny ze stroną pdf. Tutaj można dokładnie ustawić nachylenie,
            // i pozycje tego znaku

            ColumnText.showTextAligned(stronaPdf, Element.ALIGN_CENTER,znakWodny,polozenieZnakuWodnego,15,0);
            // zapis
            stronaPdf.restoreState();
        }
        // zamknięcie obu plików pdf

        oryginalnyPdf.close();
        nowyPdf.close();
        linkNowegoPdf.close();
        //drukowanie


        PDDocument dokumentDoDruku = PDDocument.load(new File(args[1]));
        //PDDocument dokumentDoDruku = PDDocument.load(new File("C:\\Users\\mike\\Documents\\WYnik.pdf"));

        PrinterJob printerJob = PrinterJob.getPrinterJob();
        printerJob.setPageable((new PDFPageable(dokumentDoDruku)));
        PrintRequestAttributeSet atr=new HashPrintRequestAttributeSet();
        atr.add(Chromaticity.MONOCHROME);
        atr.add(Sides.ONE_SIDED);
        printerJob.print(atr);
        dokumentDoDruku.close();
        frame.setVisible(false);
        frame.dispose();











//        // tworzenie listy ustawień dla wydruku
//        PrintRequestAttributeSet listaAtrybutow = new HashPrintRequestAttributeSet();
//        // dodawanie do listy ustawienia dla drukowania jednostronnego
//        listaAtrybutow.add(Sides.ONE_SIDED);
//        // kolor czarno-bialy
//        listaAtrybutow.add(Chromaticity.MONOCHROME);
//
//        // odczyt wybranego pliku w formie tablicy bajtów
//        FileInputStream pdfDoWydruku = new FileInputStream(args[1]);
//        // FileInputStream pdfDoWydruku = new FileInputStream("C:\\Users\\mike\\Documents\\xx2.pdf");
//        // tworzenie obiektu typu DOC. Jego rolą jest opisanie dokumentu który ma zostać wydrukowany.
//        // Do inicjalizacji tego obiektu używa się implementacji interfejsu SimpleDoc. Ta implementacja
//        // ma konstruktor przyjmujący dwa argumenty: plik do wydruku (w naszym przypadku pdf) oraz
//        // klasę typu DocFlavor która ma określać jaki format danych ma być wydrukowany. Metoda InputStream
//        // wkskazuje na to ze przekazujemy strumien danych a AUTOSENSE umożliwia automatyczne rozpoznanie formatu
//        // (w naszym przypadku PDF). Trzeci argument jest opcjonalny, jest nim lista atrybutów wydruku ale ją
//        // przekazujemy później więc tutaj dałem null
//        Doc pdfDoc = new SimpleDoc(pdfDoWydruku, DocFlavor.INPUT_STREAM.AUTOSENSE, null);
//
//        // JAVA wykrywa domyślną drukarkę
//        PrintService drukarkaDomyslna=PrintServiceLookup.lookupDefaultPrintService();
//        // inicjalizacja zlecenia do drukarki i wydruk za pomocą metody print. Pierwszym parametrem jest
//        // strumien danych który chcemy wyrdukować a drugim to ustawienia drukarki
//        DocPrintJob printJob = drukarkaDomyslna.createPrintJob();
//        printJob.print(pdfDoc, listaAtrybutow);
//        // zamknięcie strumienia danych
//        pdfDoWydruku.close();


    }

//    private static PdfStamper konwersjaPdf(PdfReader oryginalnyPdf, String linkNowegoPdf, String [] arrList) throws DocumentException, IOException {
//
//
//
//        PdfImportedPage page;
//            Document document = new Document(PageSize.A4);
//
//        String link = linkNowegoPdf.substring(0, linkNowegoPdf.length() - 4).concat("temp.pdf");
//         fileOutputStream=new FileOutputStream(link);
//        PdfWriter writer=PdfWriter.getInstance(document,fileOutputStream);
//            document.open();
//
//        for (int i = 0; i <arrList.length ; i++) {
//
//
//            page=writer.getImportedPage(oryginalnyPdf,Integer.parseInt(arrList[i]));
//            System.out.println(page.getWidth());
//
//
//            writer.getDirectContent().addTemplate(page,0,0);
//
//                document.newPage();
//        }
//        document.close();
//        //writer.close();
////        oryginalnyPdf.close();
//        PdfReader reader=new PdfReader(link);
//        fileOutputStream=new FileOutputStream(linkNowegoPdf);
//
//        PdfStamper stamper = new PdfStamper(reader, fileOutputStream);
//        reader.close();
//        //fileOutputStream.close();
//
//        return stamper;
//    }


    private static List<Integer> konwersjaArgumentowZVBA(String[] listaArgumentow, int numberOfPages){

        List<Integer>bufor=new ArrayList<>();
        for (int i = 0; i <listaArgumentow.length ; i++) {

            if(Integer.parseInt(listaArgumentow[i])<=numberOfPages){
                bufor.add(Integer.parseInt(listaArgumentow[i]));
            }

        }
        return bufor;

    }


}
