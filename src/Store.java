public class Store {
private String name;
private ArrayList<Product> products;

  public Store(String name) {
    //declares without file; manually adds products later
    this.name = name;
  }
  public Store(String name, String filename) {
     this.name = name;
//TODO: read from file, add to products list

}
