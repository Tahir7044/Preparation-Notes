import React, { useEffect, useRef, useState } from "react";
import "./Autocomplete.css";
import mockData from "../../data/suggestions.json";

type suggestion = {
  name: string;
  id: number;
};

const ListItem = ({ children }: { children: React.ReactNode }) => {
  return <>{children}</>;
};

const debounce = <T,>(fn:Function, delay:number)=>{
	let timer: number;
	return (...args:any): Promise<T>=>{
		return new Promise<T>((resolve, reject)=>{
			clearTimeout(timer);
			timer = setTimeout(()=> {
				try {
					const result = fn(...args);
					resolve(result);		
				} catch (error) {
					reject(error)
				}
			}, delay)
		})
	}
}

// TODO: replace with real API call: fetch(`/api/suggestions?q=${query}`).then(r => r.json())
const fetchSuggestions = (query: string): Promise<suggestion[]> => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve(mockData.filter((s) => s.name.toLowerCase().includes(query.toLowerCase())));
    }, 300);
  });
};

export const Autocomplete = () => {

	const containerRef = useRef<HTMLDivElement>(null);
	const [filteredSuggestions, setFilteredSuggestions] = useState<suggestion[]>([]);
	const [loading, setLoading] = useState(false);
	const [query, setQuery] = useState("");
	const [selectedSuggestion, setSelectedSuggestion] = useState("");
	const deBouncedSuggestions = useRef(debounce<suggestion[]>(fetchSuggestions, 500)).current;

	useEffect(() => {
		if (!query) {
			setFilteredSuggestions([]);
			return;
		}
		setLoading(true);
		deBouncedSuggestions(query)
		.then((data) => {
			setFilteredSuggestions(data);
			setLoading(false);
		})
		.catch(err=> console.log(err))
	}, [query]);


	const selectSuggestion = (suggestion: suggestion) => {
		setSelectedSuggestion(suggestion.name);
		setQuery("");
	};

	const handleClear = () => {
		setQuery("");
		setSelectedSuggestion("");
	};

	// advanced
	useEffect(() => {
	const handleOutsideClick = (e: MouseEvent) => {
		if (containerRef.current && !containerRef.current.contains(e.target as Node)) {
			setQuery("");
		}
	};
	document.addEventListener("mousedown", handleOutsideClick);
	return () => document.removeEventListener("mousedown", handleOutsideClick);
	}, []);

  return (
    <div className="container">
      <div className="search-section" ref={containerRef}>
        <input
          type="text"
          value={selectedSuggestion || query}
          name="input-search"
          id="input-search"
          placeholder="Search..."
          onChange={(e) => {
            setSelectedSuggestion("");
            setQuery(e.target.value);
          }}
        />
        <span className="clear-query" onClick={handleClear}>
          X
        </span>
        {query != "" && (
          <div className="suggestion-section">
            {loading ? (
              <ListItem>
                <div className="no-suggestion"><span>Loading...</span></div>
              </ListItem>
            ) : filteredSuggestions.length ? (
              filteredSuggestions.map((suggestion) => {
                const ind = suggestion.name
                  .toLowerCase()
                  .indexOf(query.toLowerCase());
                return (
                  <ListItem key={suggestion.id}>
                    <div
                      className="item"
                      onClick={() => selectSuggestion(suggestion)}
                    >
                      <span>{suggestion.name.slice(0, ind)}</span>
                      <span style={{ fontWeight: 600 }}>
                        {suggestion.name.slice(ind, ind + query.length)}
                      </span>
                      <span>{suggestion.name.slice(ind + query.length)}</span>
                    </div>
                  </ListItem>
                );
              })
            ) : (
              <ListItem>
                <div className="no-suggestion">
                  <span>no suggestions found</span>
                </div>
              </ListItem>
            )}
          </div>
        )}
      </div>
    </div>
  );
};
