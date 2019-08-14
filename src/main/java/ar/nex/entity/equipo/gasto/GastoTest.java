/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.entity.equipo.gasto;

import ar.nex.entity.empleado.Persona;
import ar.nex.service.JpaService;
import java.util.List;

/**
 *
 * @author Renzo
 */
public class GastoTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
              try {
            JpaService jpa = new JpaService();

            List<Gasto> lst = jpa.getGasto().findGastoEntities();
            for (Gasto e : lst) {
                System.out.println("ar.nex.entity.empleado.EmpleadoTEst.main(): " + e.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
