/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

/**
 *
 * @author Windows
 */
public class GiaoDeThiDTO {
    private int made;
    private int malop;

    public GiaoDeThiDTO() {
    }

    public GiaoDeThiDTO(int made, int malop) {
        this.made = made;
        this.malop = malop;
    }
           
    // Getters, Setters
    public int getMade() {
        return made;
    }

    public void setMade(int made) {
        this.made = made;
    }

    public int getMalop() {
        return malop;
    }

    public void setMalop(int malop) {
        this.malop = malop;
    }
    
}