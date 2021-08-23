import {Category} from "./category";

export class Product{
  id: number;
  category: Category;
  name: string;
  price: number;
  imageUrl: string;
  available: boolean;


  constructor(category: Category, name: string, price: number, imageUrl: string, available: boolean) {
    this.category = category;
    this.name = name;
    this.price = price;
    this.imageUrl = imageUrl;
    this.available = available;
  }

  setCategory(category: Category): void{
    this.category = category;
  }
}
