package guru.springframework.services;

import java.util.logging.Logger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.DateTime;


import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import guru.springframework.domain.FunData;
import guru.springframework.domain.HistoricalData;
import rx.Observable;

public class LoadCSVService implements LoadService {

	private String histDataPath;

	private String funDataPath;

	private Map<String, SortedMap<DateTime, HistoricalData>> dataset = new HashMap<String, SortedMap<DateTime, HistoricalData>>();

	private Map<String, SortedMap<DateTime, FunData>> funData = new HashMap<String, SortedMap<DateTime, FunData>>();

	public void setDataPath(String histDataPath, String funDataPath) {
		this.histDataPath = histDataPath;
		this.funDataPath = funDataPath;
	}


	@Override
	public Observable<Pair<DateTime, HistoricalData>> loadOneSymbol(String symbol) throws IOException {

		String current = new java.io.File(".").getCanonicalPath();


		SortedMap<DateTime, HistoricalData> result = CSVReaderRunner.readCsvForOneSymbol(histDataPath, symbol);

		List<Pair<DateTime, HistoricalData>> list = new ArrayList<Pair<DateTime, HistoricalData>>();

		for (Iterator<DateTime> i = result.keySet().iterator(); i.hasNext();) {
			DateTime item = i.next();
			
			Pair<DateTime, HistoricalData> p = new ImmutablePair(item, result.get(item));
			list.add(p);
		}
		return Observable.from(list);

	}

	public Observable<Pair<DateTime, FunData>> loadOneSymbolFun(String symbol) throws IOException {
		SortedMap<DateTime, FunData> result = CSVReaderRunner.readCsvFunForOneSymbol(funDataPath, symbol);

		List<Pair<DateTime, FunData>> list = new ArrayList<Pair<DateTime, FunData>>();

		for (Iterator<DateTime> i = result.keySet().iterator(); i.hasNext();) {
			DateTime item = i.next();
			
			Pair<DateTime, FunData> p = new ImmutablePair(item, result.get(item));
			list.add(p);
		}
		return Observable.from(list);

	}

	public List<String> loadSymbols(String path) throws IOException{
		return CSVReaderRunner.loadSymbols(path);

	
		
	}


}
