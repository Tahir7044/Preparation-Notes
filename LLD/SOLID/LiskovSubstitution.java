package SOLID;

/*
 * it says that we should replace the parent object with the child object without breaking the correctness of program.
 *  whatever a parent can do a child should also do that without any error.
 * 
 * 
 */


/*
 * 
 * here we can see that behavior of TextDocument class function (print) is different since
 * it is not supported in TextDocument class which is violating the Liskov Substitution principle
 * 
 * 
 */


interface IDocument {
    void open(String fileName);
    void save(String fileName);
    void print(String fileName);
}

class TextDocument implements IDocument {
    public void open(String fileName) {
        System.out.println("open text file "+fileName);
    }
    public void save(String fileName) {
        System.out.println("save text file "+fileName);
    }
    public void print(String fileName) {
       throw new  UnsupportedOperationException("print is not support");
    }
}

class PDFDocument implements IDocument {
    public void open(String fileName) {
        System.out.println("open PDF file "+fileName);
    }
    public void save(String fileName) {
        System.out.println("save PDF file "+fileName);
    }
    public void print(String fileName) {
        System.out.println("print PDF file "+fileName);
    }
}

class WordDocument implements IDocument {
    public void open(String fileName) {
        System.out.println("open word file "+fileName);
    }
    public void save(String fileName) {
        System.out.println("save word file "+fileName);
    }
    public void print(String fileName) {
        System.out.println("print word file "+fileName);
    }
}


// ------------------------------------------------------- Solution ----------------------------------------------------------


interface ILKDocument {
    void open(String fileName);
    void save(String fileName);
}

interface IPrintableDocument extends ILKDocument {
    void print(String fileName);
}

class LKTextDocument implements ILKDocument {
    public void open(String fileName) {
        System.out.println("open text file "+fileName);
    }
    public void save(String fileName) {
        System.out.println("save text file "+fileName);
    }
}

class LKPDFDocument implements IPrintableDocument {
    public void open(String fileName) {
        System.out.println("open PDF file "+fileName);
    }
    public void save(String fileName) {
        System.out.println("save PDF file "+fileName);
    }
    public void print(String fileName) {
        System.out.println("print PDF file "+fileName);
    }
}

class LKWordDocument implements IPrintableDocument {
    public void open(String fileName) {
        System.out.println("open word file "+fileName);
    }
    public void save(String fileName) {
        System.out.println("save word file "+fileName);
    }
    public void print(String fileName) {
        System.out.println("print word file "+fileName);
    }
}


public class LiskovSubstitution {
    public static void main(String[] args) {
        // this will throw the error because print is not supported in TextDocument class. it is violating LS principle.
        // IDocument document = new PDFDocument();
        // document.print("solid/pdf");
        // document = new TextDocument();
        // document.print("solid/pdf"); 


        IPrintableDocument document = new LKPDFDocument();
        document.save("solid/pdf");
        document.print("solid/pdf");

        ILKDocument textDocument = new LKTextDocument();
        textDocument.open("solid/pdf");
        // textDocument.print("solid/pdf"); not supported

    }    
}
