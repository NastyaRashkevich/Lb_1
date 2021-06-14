/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication4;

import entity.Studentyi;
import entity.Gruppyi;
import hiber.NewHibernateUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class JavaApplication4 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SessionFactory sf = NewHibernateUtil.getSessionFactory();
        Session s = sf.openSession();
        Query query = s.createQuery("From Studentyi");
        Query query1 = s.createQuery("From Gruppyi");
        List <Studentyi> stds = query.list();
        List <Gruppyi> grup = query1.list();
        for(Studentyi st: stds){
            System.out.println(st.getFamiliya() + " " + st.getImya() + " " + st.getOtchestvo() +
                    " учится в группе " + st.getGruppyi().getNazvanie());
        }
        System.out.println("-------------------------------------------");
        for(Gruppyi g: grup){
            System.out.println("Группа " + g.getNazvanie() + " имеет " + g.getStudentyis().size() + " студента(ов).");
        } 
        System.out.println("-------------------------------------------");
        List<String> ls = new ArrayList<>();
        List<String> out;
        Scanner scanner;
        for (Gruppyi m : grup) {
            scanner = new Scanner(m.getNazvanie()).useDelimiter("-");
            ls.add(scanner.next());
        }
        ls = ls.stream().distinct().collect(Collectors.toList());
        for (String ss : ls) {
            out = new ArrayList<>();
            for (int i = 0; i < grup.size(); i++) {
                if (grup.get(i).getNazvanie().contains(ss)) {
                    out.add(grup.get(i).getNazvanie());
                }
            }
            System.out.print("Группа " + ss + " включает: ");
            for (String str : out) {
                if(!str.equals(out.get(out.size() - 1))){
                    System.out.print(str + ", ");
                }
                else System.out.print(str + ".");
            }
            System.out.println();
        }
        System.out.println("-------------------------------------------");
        for(Studentyi st: stds){
              if(st.getStatus().equals("academic_leave")){
                  for(Gruppyi gr: grup){
                      if(st.getGruppyi().getKodPlana()==gr.getKodPlana() && Integer.parseInt(gr.getNazvanie().split("-",2)[1])
                              == Integer.parseInt(st.getGruppyi().getNazvanie().split("-",2)[1]) - 1 && Integer.parseInt(st.getGruppyi().getNazvanie().split("-",2)[1]) > 1){
                        Transaction tx = s.beginTransaction();
                        Studentyi student = (Studentyi) s.load(Studentyi.class, st.getNomerZachetki());
                        student.setGruppyi(gr);
                        s.update(student);
                        tx.commit(); 
                        break;
                  }
                    
               }
                  
            }
             
        }
            s.close();
            sf.close();
    }
}
