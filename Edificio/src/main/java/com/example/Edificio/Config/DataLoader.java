package com.example.Edificio.Config;

import com.example.Edificio.model.Edificio;
import com.example.Edificio.model.Estado;
import com.example.Edificio.model.Persona;
import com.example.Edificio.repository.EdificioRepository;
import com.example.Edificio.repository.PersonaRepository;
import com.example.Edificio.service.AccesoService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class DataLoader implements CommandLineRunner {
    private final EdificioRepository ed;
    private final PersonaRepository pr;

    @Override
    public void run(String... args) throws Exception {
        if (ed.count() > 0)
            return;
        var e1 = ed.save(new Edificio("ED001", "Torre Central", 2));
        var e2 = ed.save(new Edificio("ED002", "Edificio Los Condes", 1));
        var e3 = ed.save(new Edificio("ED003", "Centro Empresarial Sur", 1));
        var e4 = ed.save(new Edificio("ED004", "Torre Norte", 1));

        pr.saveAll(List.of(
                p(e1, "Juan Pérez", "223162485", Estado.DENTRO, "08:30"),
                p(e1, "María Gómez", "719676766", Estado.FUERA, "12:15"),
                p(e1, "Carlos Silva", "267824045", Estado.DENTRO, "09:05"),
                p(e2, "Ana Torres", "17252043K", Estado.DENTRO, "10:00"),
                p(e2, "Pedro Morales", "176520288", Estado.FUERA, "11:40"),
                p(e3, "Fernandito Silva", "213020218", Estado.DENTRO, "10:00"),
                p(e3, "Anthony Miles", "212030201", Estado.FUERA, "11:40"),
                p(e4, "Hernan Hernan", "281654926", Estado.DENTRO, "13:00"),
                p(e4, "Pancho Floo", "216202023", Estado.FUERA, "13:50")));
    }

    private Persona p(Edificio e, String n, String r, Estado s, String h) {
        return Persona.builder().edificio(e).nomPersona(n).rut(r).estado(s).hora(h).visitante(false).build();
    }
}
