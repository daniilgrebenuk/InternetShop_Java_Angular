import {Product} from "./product";

export interface Order {
  id: number;
  products: Product[];
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  address: string;
}
