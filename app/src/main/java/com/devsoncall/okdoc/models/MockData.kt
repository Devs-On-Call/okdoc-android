package com.devsoncall.okdoc.models

class MockData {
}

fun main(){
    val hospital1 = Hospital("1", "Grey Sloan", "Seattle Road 125", "1234567890", "greysloan@hospital.com")
    val hospital2 = Hospital("2", "Princeton-Plainsboro Hospital", "New Jersey Road 123", "1234567889", "princeton@hospital.com")
    val hospital3 = Hospital("3", "General Hospital Agios Dimitrios", "Elenis Zografou 2", "2313322100", "agdim-gei@3ype.gr")
    val hospital4 = Hospital("4", "Papanikolaou Hospital", "Exochi Area", "2313307000", "info.gpapanikolaou@n3.syzefxis.gov.gr")
    val hospital5 = Hospital("5", "Unversity Hospital Alexandroupoli", "Dragana Area", "2551353000", "infopgna@pgna.gr")
    val hospital6 = Hospital("6", "Hatzikosta Hospital Ioannina", "Leoforos Makrugianni 60", "2651366111", "manager@gni-hatzikosta.gr")



    val profession1 = Profession("1", "Dermatology")
    val profession2 = Profession("2", "Diagnostic Medicine")
    val profession3 = Profession("3", "General Surgery")
    val profession4 = Profession("4", "Neurology")
    val profession5 = Profession("5", "Obstetrics & Gynecology")
    val profession6 = Profession("6", "Ophthalmology")
    val profession7 = Profession("7", "Pathology")
    val profession8 = Profession("8", "Pediatrics")
    val profession9 = Profession("9", "Psychiatry")


    val doctor1 = Doctor("1", profession1, "Meredith", "Grey", hospital1)
    val doctor2 = Doctor("2", profession2, "Gregory", "House", hospital2)
    val doctor2 = Doctor("2", profession2, "Gregory", "House", hospital2)
    val doctor2 = Doctor("2", profession2, "Gregory", "House", hospital2)
    val doctor2 = Doctor("2", profession2, "Gregory", "House", hospital2)
    val doctor2 = Doctor("2", profession2, "Gregory", "House", hospital2)
    val doctor2 = Doctor("2", profession2, "Gregory", "House", hospital2)
    val doctor2 = Doctor("2", profession2, "Gregory", "House", hospital2)
    val doctor2 = Doctor("2", profession2, "Gregory", "House", hospital2)
    val doctor2 = Doctor("2", profession2, "Gregory", "House", hospital2)
    val doctor2 = Doctor("2", profession2, "Gregory", "House", hospital2)



    val diagnosis1 = Diagnosis("1", "01/01/2021", "Influenza", doctor1,
                    " Patient should take 2 pills of oseltamivir phosphate, 2 times per day, for 7 days")
    val diagnosis2 = Diagnosis("2", "02/02/2021", "Lupus", doctor2,
        " Patient should take 1 pill of hydroxychloroquine, 1 times per day, for lifetime")


    val prescription1 = Prescription("1", "01/01/2021", diagnosis1, "2x2", "oseltamivir phosphate", "7 days", doctor1)
    val prescription2 = Prescription("2", "02/02/2021", diagnosis2, "1x1", "hydroxychloroquine", "lifetime", doctor2)









}