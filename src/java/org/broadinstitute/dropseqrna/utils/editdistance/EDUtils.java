package org.broadinstitute.dropseqrna.utils.editdistance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.broadinstitute.dropseqrna.TranscriptomeException;
import org.broadinstitute.dropseqrna.utils.ObjectCounter;

public class EDUtils {
	
	// private final Log log = Log.getInstance(EDUtils.class);
	
	private static EDUtils singletonInstance;
	
	
	public static EDUtils getInstance() {
		if (null == singletonInstance) {
			singletonInstance = new EDUtils();
		}
		return singletonInstance;
	}

	private EDUtils() {
	}
	
	public Set<String> getStringsWithinEditDistanceWithIndel(String baseString,
			List<String> comparisonStrings, int editDistance) {
		Set<String> result = new HashSet<String>();
		for (String b : comparisonStrings) {
			int ed = LevenshteinDistance.getIndelSlidingWindowEditDistance(baseString, b);
			if (ed <= editDistance)
				result.add(b);
		}
		return (result);
	}
	
	public Set<String> getStringsWithinEditDistance(String baseString,
			List<String> comparisonStrings, int editDistance) {
		Set<String> result = new HashSet<String>();
		for (String b : comparisonStrings) {
			int ed = HammingDistance.getHammingDistance(baseString, b);
			if (ed <= editDistance)
				result.add(b);
		}
		return (result);
	}
	
	
	public Set<String> getStringsWithinHammingDistance(String baseString,
			List<String> comparisonStrings, int editDistance) {
		Set<String> result = new HashSet<String>();
		for (String b : comparisonStrings) {
			boolean flag = HammingDistance.greaterThanHammingDistance(baseString, b, editDistance);
			if (flag==false)
				result.add(b);
		}
		return (result);
	}
	
	
	/**
	 * 
	 * @param aFile The input file to read.  2 columns, the number of observations of that barcode followed by the barcode sequence. Tab seperated.
	 * @return a list of Barcodes with counts.
	 */
	public static ObjectCounter <String> readBarCodeFile(File aFile) {
		ObjectCounter <String> result = new ObjectCounter<String>();

		try {
			BufferedReader input = new BufferedReader(new FileReader(aFile));
			try {
				String line = null; // not declared within while loop
				while ((line = input.readLine()) != null) {
					line=line.trim();
					String[] strLine = line.split("\t");
					int count = Integer.parseInt(strLine[0]);
					String barcode = strLine[1].toUpperCase();
					result.incrementByCount(barcode, count);
				}
			} finally {
				input.close();
			}
		} catch (IOException ex) {
			throw new TranscriptomeException("Could not read file: "
					+ aFile.toString());
		}

		return (result);
	}
		
	
}