package com.usco.esteban.agenda_procesos.app.validation;

import java.util.Calendar;

public class CalculaDiasHabiles
{
	 public static int diasHabiles(Calendar fechaInicial, Calendar fechaFinal)
	    {
	        int diasHabiles = 0;

	        //mientras la fecha inicial sea menor o igual que la fecha final se cuentan los dias
	        while (fechaInicial.before(fechaFinal) || fechaInicial.equals(fechaFinal))
	        {
	            //System.out.println("FECHA INICIAL: " + fechaInicial.getTime());

	            int mes = fechaInicial.get(Calendar.MONTH);
	            int dia = fechaInicial.get(Calendar.DATE);
	            if(fechaInicial.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && fechaInicial.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
	                    && !(mes == 0 && dia == 1) && !(mes == 4 && dia == 1) &&!(mes == 6 && dia == 20) &&!(mes == 7 && dia == 7)  && !(mes == 11 && dia == 8)
	                    && !(mes == 11 && dia == 25) /*segundo bloque*/ && !(mes == 0 && dia == 6) && !(mes == 2 && dia == 23) && !(mes == 5 && dia == 29)
	                    && !(mes == 7 && dia == 15) && !(mes == 9 && dia == 12) && !(mes == 10 && dia == 2) &&!(mes == 10 && dia == 16)
	                    /* semana santa */ &&! (mes == 3 && dia == 9) && !(mes == 3 && dia == 10)  && !(mes == 3 && dia == 6) && !(mes == 3 && dia == 7) 
	                    && !(mes == 3 && dia == 8) /*fin semana santa*/ && !(mes == 4 && dia == 25) && !(mes == 5 && dia == 15) && !(mes == 5 && dia == 22))
	            {
	                //se aumentan los dias de diferencia entre min y max
	                diasHabiles++;
	            }
	            //se suma 1 dia para hacer la validacion del siguiente dia.
	            fechaInicial.add(Calendar.DATE, 1);
	        }
	        return diasHabiles;
	    }
}
