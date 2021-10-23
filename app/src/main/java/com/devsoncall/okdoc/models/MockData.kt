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
    val profession10 = Profession("10", "Plastic Surgery")


    val doctor1 = Doctor("1", profession3, "Meredith", "Grey", hospital1)
    val doctor2 = Doctor("2", profession4, "Derek", "Shepherd", hospital1)
    val doctor3 = Doctor("3", profession3, "Miranda", "Bailey", hospital1)
    val doctor4 = Doctor("4", profession5, "Arizona", "Robbins", hospital1)
    val doctor5 = Doctor("5", profession7, "Richard", "Webber", hospital1)
    val doctor6 = Doctor("6", profession10, "Jackson", "Avery", hospital1)
    val doctor7 = Doctor("7", profession5, "Jo", "Wilson", hospital1)
    val doctor8 = Doctor("8", profession8, "Christina", "Yang", hospital1)
    val doctor9 = Doctor("9", profession8, "Alex", "Karev", hospital1)
    val doctor10 = Doctor("10", profession9, "Owen", "Hunt", hospital1)

    val doctor11 = Doctor("11", profession5, "Callie", "Torres", hospital1)
    val doctor12 = Doctor("12", profession2, "Maggie", "Pierce", hospital3)
    val doctor13 = Doctor("13", profession10, "Mark", "Sloan", hospital3)
    val doctor14 = Doctor("14", profession9, "Andrew", "DeLuca", hospital3)
    val doctor15 = Doctor("15", profession1, "Ben", "Warren", hospital3)
    val doctor16 = Doctor("16", profession1, "Amelia", "Shepherd", hospital3)
    val doctor17 = Doctor("17", profession4, "Izzie", "Stevens", hospital3)
    val doctor18 = Doctor("18", profession8, "George", "Omalley", hospital3)
    val doctor19 = Doctor("19", profession5, "Catherine", "Avery", hospital3)
    val doctor20 = Doctor("20", profession3, "Catherine", "Avery", hospital3)


    val doctor21 = Doctor("21", profession2, "Gregory", "House", hospital2)
    val doctor22 = Doctor("22", profession9, "Eric", "Foreman", hospital2)
    val doctor23 = Doctor("23", profession3, "James", "Wilson", hospital2)
    val doctor24 = Doctor("24", profession4, "Robert", "Chase", hospital2)
    val doctor25 = Doctor("25", profession9, "Lisa", "Cuddy", hospital2)
    val doctor26 = Doctor("26", profession10, "Alison", "Cameron", hospital2)
    val doctor27 = Doctor("27", profession8, "Chris", "Taub", hospital2)
    val doctor28 = Doctor("28", profession7, "Lawrence", "Kutner", hospital2)
    val doctor29 = Doctor("29", profession8, "Jessica", "Adams", hospital2)
    val doctor30 = Doctor("30", profession8, "Stacy", "Warner", hospital2)



    val prescription1 = Prescription("1", "01/01/2021",  "20 mg - 2 times/day", "Oseltamivir phosphate", "7 days", doctor7)
    val prescription2 = Prescription("2", "10/01/2021", "100mg  - 3 times/day", "Hydroxychloroquine", "2 days", doctor5)
    val prescription3 = Prescription("3", "11/02/2021", "100 ml - 2 times/day", "Clarithromycin", "3 days", doctor21)
    val prescription4 = Prescription("4", "15/02/2021", "250 mg  - 2 times/day", "Alprazolam", "5 days", doctor2)
    val prescription5 = Prescription("5", "20/03/2021", "100 mg - 2 times/day", "Doxycycline", "7 days", doctor4)
    val prescription6 = Prescription("6", "14/04/2021", "15 ml - 2 times/day", "Pefloxacin", "3 days", doctor18)
    val prescription7 = Prescription("7", "18/04/2021", "50 mg - 3 times/day", "Amoxicillin", "4 days", doctor19)
    val prescription8 = Prescription("8", "21/04/2021", "50 mg - 1 times/day", "Saccharated iron oxide", "every day for 2 months", doctor5)
    val prescription9 = Prescription("9", "05/05/2021", "10 ml - 2 times/day", "Cyproterone Acetate", "5 days", doctor7)
    val prescription10 = Prescription("10","21/05/2021", "5 ml - 4 times/day", "Isosorbide mononitrate", "every day for lifetime", doctor12)
//    val prescription11 = Prescription("11", "02/06/2021", "25 mg - 3 times/day", "hydroxychloroquine", "3 days", doctor15)
//    val prescription12 = Prescription("12", "12/06/2021", "1000 mg - 2 times/day", "hydroxychloroquine", "every day for lifetime", doctor17)
//    val prescription13 = Prescription("13", "19/06/2021", "200 mg - 2 times/day", "hydroxychloroquine", "14 days", doctor21)
//    val prescription14 = Prescription("14", "27/07/2021", "150 mg - 4 times/day", "hydroxychloroquine", "every day for 3 months", doctor25)
//    val prescription15 = Prescription("15", "15/09/2021", "10 ml - 3 times/day", "hydroxychloroquine", "every day for lifetime", doctor24)


    val diagnosis1 = Diagnosis("1", "01/01/2021", "Influenza", doctor1, prescription1, "Patient had the following symptoms: \n" +
            "fever\n" +
            "cough.\n" +
            "sore throat.\n" +
            "runny nose.\n" +
            "muscle aches.\n" +
            "headaches.\n" +
            "fatigue (tiredness) \n"  +
            "The diagnosis was influenza")

    val diagnosis2 = Diagnosis("2", "02/02/2021", "Lupus", doctor21, prescription2, "Patient had the following symptoms: \n" +
            "High Fever \n" +
            "Rashes \n" +
            "Chest pain \n" +
            "Hair loss \n" +
            "Sun sensitivity \n" +
            "Kidney problems \n" +
            "Mouth sores")

    val diagnosis3 = Diagnosis("2", "02/02/2021", "Streptococcal pharyngitis and amygdalitis", doctor21, prescription2, "Patient had the following symptoms: \n" +
            "High Fever \n" +
            "Sore Throat \n" +
            "Red Tonsils \n" +
            "Enlarged lymph nodes \n" +
            "The diagnosis was streptococcal pharyngitis and amygdalitis")

















}