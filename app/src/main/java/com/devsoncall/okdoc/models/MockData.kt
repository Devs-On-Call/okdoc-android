package com.devsoncall.okdoc.models

class MockData {
}

fun main(){
    val hospital1 = Hospital("1", "Grey Sloan", "Seattle Road 125", "1234567890", "greysloan@hospital.com")
    val hospital2 = Hospital("2", "Princeton-Plainsboro Hospital", "New Jersey Road 123", "1234567889", "princeton@hospital.com")
    val hospital3 = Hospital("3", "General Hospital Agios Dimitrios", "Elenis Zografou 2", "1234567888", "saintjim@hospital.com")



    val profession1 = Profession("1", "Dermatology")
    val profession2 = Profession("2", "Diagnostic Medicine")
    val profession3 = Profession("3", "General Surgery")
    val profession4 = Profession("4", "Neurology")
    val profession5 = Profession("5", "Obstetrics & Gynecology")
    val profession6 = Profession("6", "Ophthalmology")
    val profession7 = Profession("7", "Pathology")
    val profession8 = Profession("8", "Pediatrics")
    val profession9 = Profession("9", "Psychiatry")


    val doctor1 = Doctor("1", profession3, "Meredith", "Grey", hospital1)
    val doctor2 = Doctor("2", profession4, "Derek", "Shepherd", hospital1)
    val doctor3 = Doctor("2", profession3, "Miranda", "Bailey", hospital1)
    val doctor4 = Doctor("2", profession5, "Arizona", "Robbins", hospital1)
    val doctor5 = Doctor("2", profession7, "Richard", "Webber", hospital1)
    val doctor6 = Doctor("2", profession6, "Jackson", "Avery", hospital1)
    val doctor7 = Doctor("2", profession5, "Jo", "Wilson", hospital1)
    val doctor8 = Doctor("2", profession8, "Christina", "Yang", hospital1)
    val doctor9 = Doctor("2", profession8, "Alex", "Karev", hospital1)
    val doctor10 = Doctor("2", profession9, "Owen", "Hunt", hospital1)
    val doctor11 = Doctor("2", profession5, "Callie", "Torres", hospital1)

    val doctor12 = Doctor("2", profession2, "Maggie", "Pierce", hospital3)
    val doctor13 = Doctor("2", profession3, "Mark", "Sloan", hospital3)
    val doctor14 = Doctor("2", profession9, "Andrew", "DeLuca", hospital3)
    val doctor15 = Doctor("2", profession1, "Ben", "Warren", hospital3)
    val doctor16 = Doctor("2", profession1, "Amelia", "Shepherd", hospital3)
    val doctor17 = Doctor("2", profession4, "Izzie", "Stevens", hospital3)
    val doctor18 = Doctor("2", profession8, "George", "Omalley", hospital3)
    val doctor19 = Doctor("2", profession5, "Catherine", "Avery", hospital3)


    val doctor20 = Doctor("2", profession5, "Gregory", "House", hospital2)




    val diagnosis1 = Diagnosis("1", "01/01/2021", "Influenza", doctor1,
                    " Patient should take 2 pills of oseltamivir phosphate, 2 times per day, for 7 days")
    val diagnosis2 = Diagnosis("2", "02/02/2021", "Lupus", doctor2,
        " Patient should take 1 pill of hydroxychloroquine, 1 times per day, for lifetime")


    val prescription1 = Prescription("1", "01/01/2021", diagnosis1, "2x2", "oseltamivir phosphate", "7 days", doctor1)
    val prescription2 = Prescription("2", "02/02/2021", diagnosis2, "1x1", "hydroxychloroquine", "lifetime", doctor2)









}