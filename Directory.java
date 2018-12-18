public class Directory extends Node {

    private Entry[] files;

    Directory(Entry... files) {
        this.files = files;
        touch();
    }

    static Directory createEmpty() {
        return new Directory();
    }

    public Entry[] getEntries() {
        return files;
    }

    boolean containsEntry(String name) {
        
        for (Entry i : files) {
            if (name.equals(i.getName())) return true;
            else if (i.getNode() instanceof Directory && i.getAsDirectory().containsEntry(name)) return true;
        }
        return false;

    }

    public Entry getEntry(String name) {

      
        for (Entry i : files) {
            if (name.equals(i.getName())) return i;
            else if(i.getNode() instanceof Directory && i.getAsDirectory().containsEntry(name)) return i.getAsDirectory().getEntry(name);
            }
        return null;

      
    }

    Entry createDirectory(String name) {
        if (this == null || containsEntry(name)) {
            return null;
        }

        Entry[] tmp = extendArray();
        tmp[tmp.length - 1] = new Entry(name, new Directory());

        files = tmp;
        return tmp[tmp.length - 1];
    }

    Entry createFile(String name, String content) {
        if (this == null || containsEntry(name)) {
            return null;
        }

        Entry[] tmp = extendArray();
        tmp[tmp.length - 1] = new Entry(name, new File(content));

        files = tmp;
        return tmp[tmp.length - 1];
    }

    Entry createHardlink(String name, Entry entry) {
        if (this == null || containsEntry(name)) {
            return null;
        }

        Entry[] tmp = extendArray();
        tmp[tmp.length - 1] = entry.createHardlink(name);

        files = tmp;
        return tmp[tmp.length - 1];
    }

    private Entry[] extendArray() {

        Entry[] tmp = new Entry[files.length + 1];

        for (int i = 0; i < files.length; i++) {
            tmp[i] = files[i];
        }

        return tmp;
    }

    void accept(String name, Visitor visitor) {
        if(this != null) {
            visitor.visitDirectory(name, this);

            for (Entry i : files) {
                if (i.getNode() instanceof File) visitor.visitFile(i.getName(), i.getAsFile());
                else if (i.getNode() instanceof Directory) i.getAsDirectory().accept(i.getName(), visitor);
            }

            visitor.visitedDirectory();
        }
    }


}