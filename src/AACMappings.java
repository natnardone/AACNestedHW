import edu.grinnell.csc207.util.AssociativeArray;

import java.io.FileWriter;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.File;

import edu.grinnell.csc207.util.KeyNotFoundException;
import edu.grinnell.csc207.util.NullKeyException;

/**
 * Creates a set of mappings of an AAC that has two levels,
 * one for categories and then within each category, it has
 * images that have associated text to be spoken. This class
 * provides the methods for interacting with the categories
 * and updating the set of images that would be shown and handling
 * an interactions.
 * 
 * @author Catie Baker & YOUR NAME HERE
 *
 */
public class AACMappings implements AACPage {

	//private AACCategory allcats; // maps filenames (of categories) to words
	private AssociativeArray<String,AACCategory> categories; // maps filenames to categories
	private String current; // current category (filename)

	/**
	 * Creates a set of mappings for the AAC based on the provided
	 * file. The file is read in to create categories and fill each
	 * of the categories with initial items. The file is formatted as
	 * the text location of the category followed by the text name of the
	 * category and then one line per item in the category that starts with
	 * > and then has the file name and text of that image
	 * 
	 * for instance:
	 * img/food/plate.png food
	 * >img/food/icons8-french-fries-96.png french fries
	 * >img/food/icons8-watermelon-96.png watermelon
	 * img/clothing/hanger.png clothing
	 * >img/clothing/collaredshirt.png collared shirt
	 * 
	 * represents the file with two categories, food and clothing
	 * and food has french fries and watermelon and clothing has a 
	 * collared shirt
	 * @param filename the name of the file that stores the mapping information
	 */
	public AACMappings(String filename) {
    try {
      Scanner scan = new Scanner(new File(filename));
      categories = new AssociativeArray<String,AACCategory>();
      String currentCat = "";
      while (scan.hasNextLine()) {
        String line = scan.nextLine();
        String[] parts = line.split(" ");

        if (parts[0].charAt(0) != '>') {
          // category
          try {
            String catDesc = parts[1];
            for (int i = 2; i < parts.length; i++) {
              catDesc = catDesc.concat(" " + parts[i]);
            }
            categories.set(parts[0], new AACCategory(catDesc));
          } catch (Exception e) {
            System.err.println("Null key");
          }
          currentCat = parts[0];
        } else {
          // item
          try {
            String imgDesc = parts[1];
            for (int i = 2; i < parts.length; i++) {
              imgDesc = imgDesc.concat(" " + parts[i]);
            }
            categories.get(currentCat).addItem(parts[0].substring(1), imgDesc);
          } catch (KeyNotFoundException e) {
            System.err.println("Key not found");
          }
        }
        // split by space
        // check first character
        // create as needed
      }
      scan.close();
      // set categories values;
      // for a line not starting with >, create new pair in categories
      // set that pair to current
      // for each line starting with >, create new pair in the AACCategory
      current = "";
    } catch (Exception e) {

    }
	}
	
	/**
	 * Given the image location selected, it determines the action to be
	 * taken. This can be updating the information that should be displayed
	 * or returning text to be spoken. If the image provided is a category, 
	 * it updates the AAC's current category to be the category associated 
	 * with that image and returns the empty string. If the AAC is currently
	 * in a category and the image provided is in that category, it returns
	 * the text to be spoken.
	 * @param imageLoc the location where the image is stored
	 * @return if there is text to be spoken, it returns that information, otherwise
	 * it returns the empty string
	 * @throws NoSuchElementException if the image provided is not in the current 
	 * category
	 */
	public String select(String imageLoc) {
    if (this.categories.hasKey(imageLoc)) {
      this.current = imageLoc;
      return "";
    } else {
      try {
        return categories.get(current).select(imageLoc);
      } catch (Exception e) {
        throw new NoSuchElementException();
      }
    }
    // if category (check allcats keys), update current category and return empty string
    // else assume image, if in current category return text to be spoken
    // else throw exception
	}
	
	/**
	 * Provides an array of all the images in the current category
	 * @return the array of images in the current category; if there are no images,
	 * it should return an empty array
	 */
	public String[] getImageLocs() {
		try {
      if (current.equals("")) {
        Object[] temp = this.categories.getKeys();
        String[] actual = new String[temp.length];
        for (int i = 0; i < temp.length; i++) {
          actual[i] = (String) temp[i];
        }
        return actual;
      } else {
        return this.categories.get(current).getImageLocs();
      }
    } catch (Exception e) {
      System.err.println(e);
      return null;
    } // returns AACCategory for that category
	}
	
	/**
	 * Resets the current category of the AAC back to the default
	 * category
	 */
	public void reset() {
    current = "";
	}
	
	
	/**
	 * Writes the ACC mappings stored to a file. The file is formatted as
	 * the text location of the category followed by the text name of the
	 * category and then one line per item in the category that starts with
	 * > and then has the file name and text of that image
	 * 
	 * for instance:
	 * img/food/plate.png food
	 * >img/food/icons8-french-fries-96.png french fries
	 * >img/food/icons8-watermelon-96.png watermelon
	 * img/clothing/hanger.png clothing
	 * >img/clothing/collaredshirt.png collared shirt
	 * 
	 * represents the file with two categories, food and clothing
	 * and food has french fries and watermelon and clothing has a 
	 * collared shirt
	 * 
	 * @param filename the name of the file to write the
	 * AAC mapping to
	 */
	public void writeToFile(String filename) {
    try {
      FileWriter writer = new FileWriter(filename);
      String[] cats = categories.getKeys();
      String[] imgs;
      for (int i = 0; i < cats.length; i++) { // loop through categories
        try {
          AACCategory cur = categories.get(cats[i]); // AACCategory that corresponds to current category
          imgs = cur.getImageLocs();
          writer.write(cats[i] + " " + cur.getCategory() + "\n");
          for (int j = 0; j < imgs.length; j++) { // loop through images
            writer.write(">" + imgs[j] + " " + cur.select(imgs[j]) + "\n");
          }
        } catch (Exception e) {

        }
      }
      writer.close();
    } catch (Exception e) {

    }
		// in categories, get first string/aaccategory pair for first line
    // then for each pair in that aaccategory, print > and the pair of filename and word on a line
    // when done with one category, continue through the others
	}
	
	/**
	 * Adds the mapping to the current category (or the default category if
	 * that is the current category)
	 * @param imageLoc the location of the image
	 * @param text the text associated with the image
	 */
	public void addItem(String imageLoc, String text) {
    try {
      if (current.equals("")) {
        categories.set(imageLoc, new AACCategory(text));
      } else {
        categories.get(current).addItem(imageLoc, text);
      }
    } catch (Exception e) {
      System.err.println("Null image location.");
    }
		// if on homescreen, create new category with image and name
    // if in category, add image and text in category
	}


	/**
	 * Gets the name of the current category
	 * @return returns the current category or the empty string if 
	 * on the default category
	 */
	public String getCategory() {
    try {
      if (current.equals("")) {
        return "";
      } else {
        return categories.get(current).getCategory();
      }
    } catch (Exception e) {
      return null;
    }
	}


	/**
	 * Determines if the provided image is in the set of images that
	 * can be displayed and false otherwise
	 * @param imageLoc the location of the category
	 * @return true if it is in the set of images that
	 * can be displayed, false otherwise
	 */
	public boolean hasImage(String imageLoc) {
		for (String img : this.getImageLocs()) {
      if (imageLoc.equals(img)) {
        return true;
      }
    }
    return false;
	}
}
