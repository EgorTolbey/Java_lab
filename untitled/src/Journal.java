import java.util.Comparator;
public class Journal {
    private final String name;
    private final double impactFactor;
    private final String category;
    private final int citation;
    private final int articals;
    private final int procentsCitations;
    private final double coefCitations;
    private final double coefCitationsInRateMags;
    private final String nameOfPublic;

    public Journal() {
        name = "DefoltMagazin";
        impactFactor = 0.0;
        category = "none";
        citation = 0;
        articals = 0;
        procentsCitations = 0;
        coefCitations = 0.0;
        coefCitationsInRateMags = 0.0;
        nameOfPublic = "user";
    }

    public Journal(String name, double impactFactor, String category, int citation,
                   int articals, int procentsCitations, double coefCitations,
                   double coefCitationsInRateMags, String nameOfPublic) throws Exception {
        if (name.isEmpty()) {
            throw new Exception("отсутствует название журнала");
        } else {
            this.name = name;
        }
        this.impactFactor = impactFactor;
        if (category.isEmpty()) {
            throw new Exception("отсутствует категория журнала");
        } else {
            this.category = category;
        }
        if (citation < 0) {
            throw new Exception("неправильное значение кол-ва цитирований статей журнала");
        } else {
            this.citation = citation;
        }
        if (articals < 0) {
            throw new Exception("неправильное значение кол-ва опубликованных статей");
        } else {
            this.articals = articals;
        }
        if (procentsCitations < 0) {
            throw new Exception("неправильное процентное соотношение цитирования по сравнению с предыдущим годом");
        } else {
            this.procentsCitations = procentsCitations;
        }
        if (coefCitations < 0.0) {
            throw new Exception("неправильный кэффрициент цитирований на основе общего количества цитрований в предметной области");
        } else {
            this.coefCitations = coefCitations;
        }
        if (coefCitationsInRateMags < 0.0) {
            throw new Exception("неправильный коэфф цитирования в рейтинговых журналах");
        } else {
            this.coefCitationsInRateMags = coefCitationsInRateMags;
        }
        if (nameOfPublic.isEmpty()) {
            throw new Exception("отсутствует название издателя журнала");
        } else {
            this.nameOfPublic = nameOfPublic;
        }
    }

    public Journal(Journal p) {
        this.name = p.name;
        this.impactFactor = p.impactFactor;
        this.category = p.category;
        this.citation = p.citation;
        this.articals = p.articals;
        this.procentsCitations = p.procentsCitations;
        this.coefCitations = p.coefCitations;
        this.coefCitationsInRateMags = p.coefCitationsInRateMags;
        this.nameOfPublic = p.nameOfPublic;
    }

    public String getName() {
        return name;
    }

    public double getImpactFactor() {
        return impactFactor;
    }

    public String getCategory() {
        return category;
    }

    public int getCitation() {
        return citation;
    }

    public int getArticals() {
        return articals;
    }

    public int getProcentsCitations() {
        return procentsCitations;
    }

    public double getCoefCitations() {
        return coefCitations;
    }

    public double getCoefCitationsInRateMags() {
        return coefCitationsInRateMags;
    }

    public double AvgCitation(){
        if(articals == 0){
            return 0.0;
        }
        return (double) citation / articals;
    }
    public String getNameOfPublic() {
        return nameOfPublic;
    }
    @Override
    public String toString(){
        return "Название: " + getName() + "\nимпакт фактор: " + getImpactFactor() + "\nкатегория: " + getCategory() +
                "\nколичество цитирований статей журнала за последение три года: " + getCitation() +
                "\nколичество опубликованных статей за последние три года: " + getArticals() +
                "\nпроцентное соотношение цитирования по сравнению с предыдущим годом: " + getProcentsCitations() +
                "\nкэффрициент цитирований на основе общего количества цитрований в предметной области: " + getCoefCitations() +
                "\nкоэфф цитирования в рейтинговых журналах: " + getCoefCitationsInRateMags() +
                "\nназвание издателя: " + getNameOfPublic();
    }
}
class CompareAVGCitations implements Comparator<Journal>{
    @Override
    public int compare(Journal p, Journal t){
        return Double.compare(t.AvgCitation(), p.AvgCitation());
    }
}
class CompareUtility implements Comparator<Journal>{
    @Override
    public int compare(Journal p, Journal t){
        double w1 = 0.5;
        double w2 = 0.0003 ;
        double w3 = 0.02;
        double z1 = p.getImpactFactor()*w1 + (p.getCitation())*w2 + p.getArticals()*w3;
        double z2 = t.getImpactFactor()*w1 + t.getCitation()*w2 + t.getArticals()*w3;
        return Double.compare(z2, z1);
    }
}