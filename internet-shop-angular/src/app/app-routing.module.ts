import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CategoryPageComponent} from "./category-page/category-page.component";
import {ProductPageComponent} from "./product-page/product-page.component";
import {RegistrationComponent} from "./registration/registration.component";
import {OrderPageComponent} from "./order-page/order-page.component";
import {ProductProfileComponent} from "./product-profile/product-profile.component";
import {FindOrderComponent} from "./find-order/find-order.component";

const routes: Routes = [
  {path: 'category', component: CategoryPageComponent},
  {path: 'category/:id', component: ProductPageComponent},
  {path: 'products', component: ProductPageComponent},
  {path: 'products/search/:name', component: ProductPageComponent},
  {path: 'product/:id', component: ProductProfileComponent},
  {path: 'register', component: RegistrationComponent},
  {path: 'order', component:OrderPageComponent},
  {path: 'find-order', component: FindOrderComponent},
  {path: 'find-order/:id', component: FindOrderComponent},
  {path: '', redirectTo: 'category', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
