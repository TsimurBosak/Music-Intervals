public class Intervals {

    private static boolean order = true;

    private static int noteGap;

    private static int semitoneGap;

    private static String[] notes = new String[]{"C", "D", "E", "F", "G", "A", "B"};

    private static String[] allowedToInputNotes = new String[]{"Cb", "C", "C#", "Db", "D", "D#", "Eb", "E", "E#", "Fb", "F",
            "F#", "Gb", "G", "G#", "Ab", "A", "A#", "Bb", "B", "B#"};
    private static String[] allowedToInputIntervals = new String[]{"m2", "M2", "m3", "M3", "P4", "P5", "m6", "M6", "m7", "M7", "P8"};


    public static String intervalConstruction(String[] args) {
        if (args.length > 3 || args.length < 2)
            throw new IllegalArgumentException("Illegal number of elements in input array");
        getNoteGapInterval(args[0]);
        if (args.length == 3) {
            if (args[2].equals("dsc")) {
                order = false;
                return getResultNoteDesc(args[1]);
            }
        }
        return getResultNoteAsc(args[1]);
    }


    public static String intervalIdentification(String[] args) {
        return "";
    }

    private static String getResultNoteAsc(String firstNote) {
        String finalNote = getClearIntervalNote(firstNote);
        if (firstNote.substring(0, 1).equals(finalNote))
            return firstNote;
        int gap = semitoneGap;
        int step;
        if (getAccidental(firstNote) != null)
            gap = changeGapByAccidentalAsc(gap, firstNote);
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
        return finalSwitch(gap, firstNote);
    }

    private static String getResultNoteDesc(String firstNote) {
        String finalNote = getClearIntervalNote(firstNote);
        if (firstNote.substring(0, 1).equals(finalNote))
            return firstNote;
        int gap = semitoneGap;
        int step;
        if (getAccidental(firstNote) != null)
            gap = changeGapByAccidentalDesc(gap, firstNote);
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
        return finalSwitch(gap, firstNote);
    }

    private static int changeGapByAccidentalAsc(int gap, String note) {
        if (note.substring(1).equals("b"))
            gap--;
        else gap++;
        return gap;
    }

    private static int changeGapByAccidentalDesc(int gap, String note) {
        if (note.substring(1).equals("b"))
            gap++;
        else gap--;
        return gap;
    }

    private static String finalSwitch(int gap, String firstNote) {
        if (!order) {
            gap *= -1;
        }
        switch (gap) {
            case -2:
                return getClearIntervalNote(firstNote) + "bb";
            case -1:
                return getClearIntervalNote(firstNote) + "b";
            case 0:
                return getClearIntervalNote(firstNote);
            case 1:
                return getClearIntervalNote(firstNote) + "#";
            case 2:
                return getClearIntervalNote(firstNote) + "##";
        }
        return null;
    }


    private static String getClearIntervalNote(String firstNote) {
        if (order) {
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
        } else {
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
        return null;
    }


    private static String getAccidental(String note) {
        if (note.length() > 1)
            return note.substring(1);
        return null;
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
}
