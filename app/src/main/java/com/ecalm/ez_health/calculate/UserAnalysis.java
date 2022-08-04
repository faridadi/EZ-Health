package com.ecalm.ez_health.calculate;

public class UserAnalysis {

    public UserAnalysis() {

    }
    public float getMaleBmr(float bb, float tb, float um) {
        float result = 88.362f + (13.397f * bb) + (4.799f * tb) - (5.677f * um);
        int round = Math.round(result);
        return (float)round;
    }

    public float getFemaleBmr(float bb, float tb, float um) {
        float result = 447.593f + (9.247f * bb) + (3.098f * tb) - (4.330f * um);
        int round = Math.round(result);
        return (float)round;
    }

    public float getBmi(float bb, float tb) {
        float result = (float)(bb/Math.pow(tb/100,2));
        return result;
    }

    public int getCategoryBmi(float bb, float tb){
        float bmiTol = getBmi(bb, tb);
        int bmi = 0;
        if(bmiTol < 16){
            bmi = 1;
        }else if(bmiTol >= 16 && bmiTol < 17){
            bmi = 2;
        }else if(bmiTol >= 17 && bmiTol < 18.5){
            bmi = 3;
        }else if(bmiTol >= 18.5 && bmiTol < 25){
            bmi = 4;
        }else if(bmiTol >= 25 && bmiTol < 30){
            bmi = 5;
        }else if(bmiTol >= 30 && bmiTol < 35){
            bmi = 6;
        }else if(bmiTol >= 35 && bmiTol < 40){
            bmi = 7;
        }else if(bmiTol >= 40){
            bmi = 8;
        }
        return bmi;
    }

    public float getTdee(float bmr, float factor, float program, float rec){
        float totCal = 0;
        totCal=(bmr*factor)+program+rec;
        return totCal;
    }

    public float getBurnedCalorie(float bb){
        float walkMet = 3.5f;
        float waktu = 1800;//detik
        float burnCalorie = (float) (walkMet * 0.0175 * bb*(waktu/60));
        return burnCalorie;
    }

    public float getMaleStride(Float height){
        float stride = height * 0.415f;
        return stride;
    }

    public float getFemaleStride(Float height){
        float stride = height * 0.413f;
        return stride;
    }

}
