public class Intervals {

    private enum Order{
            ASC,
            DSC
    }

    private static int noteGap;

    private static int semitoneGap;

    private static int startNotePosition;

    private static int endNotePosition;

    private static String[] notes = new String[]{"C", "D", "E", "F", "G", "A", "B"};

    private static String[] allowedToInputNotes = new String[]{"Cb", "C", "C#", "Db", "D", "D#", "Eb", "E", "E#", "Fb", "F",
            "F#", "Gb", "G", "G#", "Ab", "A", "A#", "Bb", "B", "B#"};

    private static String[] allowedToInputNotesExtended = new String[] {"Cbb", "Cb", "C", "C#", "C##", "Dbb", "Db", "D",
            "D#", "D##", "Ebb", "Eb", "E", "E#", "E##", "Fbb", "Fb", "F", "F#", "F##", "Gbb", "Gb", "G", "G#", "G##",
            "Abb", "Ab", "A", "A#", "A##", "Bbb", "Bb", "B", "B#", "B##"};

    public static String[] allowedToInputIntervals = new String[]{"m2", "M2", "m3", "M3", "P4", "P5", "m6", "M6", "m7", "M7", "P8"};


    public static String intervalConstruction(String[] args) {
        if (args.length > 3 || args.length < 2)
            throw new IllegalArgumentException("Illegal number of elements in input array");
        getNoteGapInterval(args[0]);
        checkIsAllowedToInputNote(args[1]);
        if (args.length == 3) {
            if (args[2].equals("dsc")) {
                return getResultNote(args[1],Order.DSC);
            }
        }
        return getResultNote(args[1],Order.ASC);
    }

    public static String intervalIdentification(String[] args) {
        if (args.length > 3 || args.length < 2)
            throw new IllegalArgumentException("Illegal number of elements in input array");
        checkIsAllowedToInputNotes(args[0],args[1]);
        if (args.length == 3) {
            if (args[2].equals("dsc")) {
                return getInterval(args[0],args[1],Order.DSC);
            }
        }
        return getInterval(args[0], args[1],Order.ASC);
    }


    private static String getResultNote(String firstNote, Order order){
        String finalNote = getClearIntervalNote(firstNote,order);
        if (firstNote.substring(0, 1).equals(finalNote))
            return firstNote;
        int gap = semitoneGap;
        if (getAccidental(firstNote) != null)
            gap = changeGapByAccidental(gap, firstNote, order);

        switch (order){
            case ASC:
                return getResultNoteAsc(firstNote,finalNote,gap,order);
            case DSC:
                return getResultNoteDsc(firstNote,finalNote,gap,order);
        }
        throw new IllegalArgumentException("unreachable command");
    }

    private static String getResultNoteAsc(String firstNote, String finalNote, int gap, Order order) {
        int step;
        outsideLoop:
        for (int i = 0; i < notes.length; i++) {
            if (firstNote.substring(0, 1).equals(notes[i])) {
                for (int j = i; j < notes.length; j++) {
                    if (notes[j].equals(finalNote) && gap < 3) {
                        break outsideLoop;
                    }
                    if (j == notes.length - 1) {
                        step = 1;
                        j = -1;
                        gap -= step;
                    } else if (notes[j].equals("E")) {
                        step = 1;
                        gap -= step;
                    } else {
                        step = 2;
                        gap -= step;
                    }
                }
            }
        }
        return finalSwitchNote(gap, firstNote, order);
    }

    private static String getResultNoteDsc(String firstNote, String finalNote, int gap, Order order) {
        int step;
        outsideLoop:
        for (int i = notes.length - 1; i >= 0; i--) {
            if (firstNote.substring(0, 1).equals(notes[i])) {
                for (int j = i; j < notes.length; j--) {
                    if (notes[j].equals(finalNote) && gap < 3) {
                        break outsideLoop;
                    }
                    if (j == 0) {
                        step = 1;
                        j = notes.length;
                        gap -= step;
                    } else if (notes[j].equals("F")) {
                        step = 1;
                        gap -= step;
                    } else {
                        step = 2;
                        gap -= step;
                    }
                }
            }
        }
        return finalSwitchNote(gap, firstNote, order);
    }

    private static int changeGapByAccidental(int gap, String note, Order order){
        if (note.substring(1).equals("b")){
            switch (order){
                case ASC:
                    gap--;
                    break;
                case DSC:
                    gap++;
                    break;
            }
        }
        else{
            switch (order){
                case ASC:
                    gap++;
                    break;
                case DSC:
                    gap--;
                    break;
            }
        }
        return gap;
    }


    private static String finalSwitchNote(int gap, String firstNote, Order order) {
        switch (order){
            case ASC:
                break;
            case DSC:
                gap *=-1;

        }
        switch (gap) {
            case -2:
                return getClearIntervalNote(firstNote,order) + "bb";
            case -1:
                return getClearIntervalNote(firstNote,order) + "b";
            case 0:
                return getClearIntervalNote(firstNote,order);
            case 1:
                return getClearIntervalNote(firstNote,order) + "#";
            case 2:
                return getClearIntervalNote(firstNote,order) + "##";
        }
        return null;
    }

    private static int getNoteGapForInterval(String firstNote, String endNote, Order order) {

        int clearNoteGap =0;

        if (firstNote.substring(0, 1).equals(endNote.substring(0, 1))) {
            clearNoteGap = 8;
            return clearNoteGap;
        }

        for (int i = 0; i < notes.length; i++) {
            if (firstNote.substring(0, 1).equals(notes[i]))
                startNotePosition = i;
            if (endNote.substring(0, 1).equals(notes[i]))
                endNotePosition = i;
        }

        switch (order){
            case ASC:
                if (endNotePosition > startNotePosition)
                    clearNoteGap = endNotePosition - startNotePosition + 1;
                else
                    clearNoteGap = notes.length - startNotePosition + endNotePosition + 1;
                break;
            case DSC:
                if (startNotePosition > endNotePosition)
                    clearNoteGap = startNotePosition - endNotePosition + 1;
                else
                    clearNoteGap = notes.length - endNotePosition + startNotePosition + 1;
                break;

        }

        if (clearNoteGap == 0)
            throw new IllegalArgumentException("Illegal input notes");
        return clearNoteGap;
    }

    private static int getAccidentalShift(String note, Order order) {
        int shift = 0;
        for (int i = 0; i < note.length(); i++) {
            if (note.charAt(i) == '#')
                shift++;
            else if (note.charAt(i) == 'b')
                shift--;
        }
        switch (order){
            case ASC:
                break;
            case DSC:
                shift *= -1;
                break;
        }
        return shift;
    }

    private static String getInterval(String firstNote,String endNote, Order order) {
        int noteGap = getNoteGapForInterval(firstNote, endNote,order);
        int firstNoteShift = getAccidentalShift(firstNote, order);
        int endNoteShift = getAccidentalShift(endNote, order);
        int semitoneGap = endNoteShift - firstNoteShift;
        if (noteGap == 8 && semitoneGap == 0)
            return "P8";
        else if (noteGap == 8)
            return getIntervalNameByGaps(noteGap, semitoneGap);
        switch (order) {
            case ASC:
                return getIntervalAsc(firstNote, noteGap, semitoneGap);
            case DSC:
                return getIntervalDsc(firstNote, noteGap, semitoneGap);

        }
        throw new IllegalArgumentException("unreachable command");
    }

    private static String getIntervalAsc(String firstNote, int noteGap, int semitoneGap) {
        int step;
        outsideLoop:
        for (int i = 0; i < notes.length; i++) {
            if (firstNote.substring(0, 1).equals(notes[i])) {
                for (int j = i; j < notes.length; j++) {
                    if (j == endNotePosition) {
                        break outsideLoop;
                    }
                    if (j == notes.length - 1) {
                        step = 1;
                        j = -1;
                        semitoneGap += step;
                    } else if (notes[j].equals("E")) {
                        step = 1;
                        semitoneGap += step;
                    } else {
                        step = 2;
                        semitoneGap += step;
                    }
                }
            }
        }
        return getIntervalNameByGaps(noteGap, semitoneGap);
    }

    private static String getIntervalDsc(String firstNote, int noteGap, int semitoneGap){
        int step;
        outsideLoop:
        for (int i = notes.length-1; i >= 0; i--) {
            if (firstNote.substring(0, 1).equals(notes[i])) {
                for (int j = i; j < notes.length; j--) {
                    if (j == endNotePosition) {
                        break outsideLoop;
                    }
                    if (j == 0) {
                        step = 1;
                        j = notes.length;
                        semitoneGap += step;
                    } else if (notes[j].equals("F")) {
                        step = 1;
                        semitoneGap += step;
                    } else {
                        step = 2;
                        semitoneGap += step;
                    }
                }
            }
        }
        return getIntervalNameByGaps(noteGap, semitoneGap);
    }


    private static String getClearIntervalNote(String firstNote, Order order) {
        switch (order){
            case ASC:
                for (int i = 0; i < notes.length; i++) {
                    if (firstNote.substring(0, 1).equals(notes[i])) {
                        if (i + noteGap > notes.length) {
                            if (notes.length - noteGap - i + 1 > 0)
                                return notes[notes.length - noteGap - i + 1];
                            return notes[noteGap + i - 1 - notes.length];
                        } else
                            return notes[i + noteGap - 1];
                    }
                }
            case DSC:
                for (int i = 0; i < notes.length; i++) {
                    if (firstNote.substring(0, 1).equals(notes[i])) {
                        if (i - noteGap < 0) {
                            if (notes.length - noteGap + i + 1 < 7)
                                return notes[notes.length - noteGap + i + 1];
                            return notes[0];
                        } else
                            return notes[i - noteGap + 1];
                    }
                }
        }
        throw new IllegalArgumentException("Illegal note name");
    }


    private static String getAccidental(String note) {
        if (note.length() > 1)
            return note.substring(1);
        return null;
    }

    private static void checkIsAllowedToInputNote(String firstNote){
        boolean isValid = false;
        for (String note:
                allowedToInputNotes) {
            if (note.equals(firstNote))
                isValid = true;
        }
        if (!isValid)
            throw new IllegalArgumentException("This note are not allowed to input");
    }

    private static void checkIsAllowedToInputNotes(String firstNote, String endNote){
        boolean isValidFirst = false;
        boolean isValidEnd = false;
        for (String note:
                allowedToInputNotesExtended) {
            if (note.equals(firstNote))
                isValidFirst = true;
            if (note.equals(endNote))
                isValidEnd = true;
        }
        if (!isValidFirst || !isValidEnd)
            throw new IllegalArgumentException("This note or both notes are not allowed to input");
    }


    private static void getNoteGapInterval(String interval) {
        switch (interval) {
            case "m2":
                noteGap = 2;
                semitoneGap = 1;
                break;
            case "M2":
                noteGap = 2;
                semitoneGap = 2;
                break;
            case "m3":
                noteGap = 3;
                semitoneGap = 3;
                break;
            case "M3":
                noteGap = 3;
                semitoneGap = 4;
                break;
            case "P4":
                noteGap = 4;
                semitoneGap = 5;
                break;
            case "P5":
                noteGap = 5;
                semitoneGap = 7;
                break;
            case "m6":
                noteGap = 6;
                semitoneGap = 8;
                break;
            case "M6":
                noteGap = 6;
                semitoneGap = 9;
                break;
            case "m7":
                noteGap = 7;
                semitoneGap = 10;
                break;
            case "M7":
                noteGap = 7;
                semitoneGap = 11;
                break;
            case "P8":
                noteGap = 8;
                semitoneGap = 12;
                break;
            default:
                throw new IllegalArgumentException("Illegal interval name");
        }
    }

    private static String getIntervalNameByGaps(int noteGap, int semitoneGap) {
        switch (noteGap) {
            case 2:
                if (semitoneGap == 1)
                    return "m2";
                if (semitoneGap == 2)
                    return "M2";
                break;
            case 3:
                if (semitoneGap == 3)
                    return "m3";
                if (semitoneGap == 4)
                    return "M3";
                break;
            case 4:
                if (semitoneGap == 5)
                    return "P4";
                break;
            case 5:
                if (semitoneGap == 7)
                    return "P5";
                break;
            case 6:
                if (semitoneGap == 8)
                    return "m6";
                if (semitoneGap == 9)
                    return "M6";
                break;
            case 7:
                if (semitoneGap == 10)
                    return "m7";
                if (semitoneGap == 11)
                    return "M7";
                break;
            case 8:
                if (semitoneGap == 12)
                    return "P8";
                break;
        }
        throw new IllegalArgumentException("Cannot identify the interval");
    }
}
