package mx.tecnm.backend.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.tecnm.backend.api.models.Product;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;

@RestController
public class Test {
    @GetMapping("/hello")
    public String helloworld(){
        return "Hola API Rest";
    }

    @GetMapping("/producto")
    public Product getProducto(){
        Product producto = new Product();
        producto.name = "Yoghurt Dannone Fresa";
        producto.codeBar = "";
        producto.price = 12.00;
        return producto;
    }

    @GetMapping("/productos")
    public Product[] getProductos(){
        Product producto1 = new Product();
        producto1.name = "Yoghurt Dannone Fresa";
        producto1.codeBar = "7501035801234";
        producto1.price = 12.00;

        Product producto2 = new Product();
        producto2.name = "Leche Lala";
        producto2.codeBar = "7501035801235";
        producto2.price = 10.00;

        return new Product[]{producto1, producto2};
    }

    @GetMapping("/productos/{id}")
    public ResponseEntity<Product> getProductoById(@PathVariable int id) {
        Product p1 = new Product();
        p1.name = "Manzana";
        p1.codeBar = "ABC123";
        p1.price = 12.5;

        Product p2 = new Product();
        p2.name = "Plátano";
        p2.codeBar = "DEF456";
        p2.price = 8.9;

        Product p3 = new Product();
        p3.name = "Naranja";
        p3.codeBar = "GHI789";
        p3.price = 10.0;

        Product[] productos = {p1, p2, p3};

        if (id < 0 || id >= productos.length) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(productos[id]);
    }
}
