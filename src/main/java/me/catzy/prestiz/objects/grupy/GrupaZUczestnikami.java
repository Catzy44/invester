package me.catzy.prestiz.objects.grupy;

import java.util.List;
import me.catzy.prestiz.objects.uczestnicy.Uczestnik;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "uczestnicy", types = {Grupa.class})
public interface GrupaZUczestnikami {
  String getNazwa();
  
  List<Uczestnik> getUczestnicy();
}
