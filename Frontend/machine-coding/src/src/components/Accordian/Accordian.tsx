import React, { useState } from "react";
import { ChevronDown } from "lucide-react";
import "./Accordian.css";

interface AccordianContent {
  id: number;
  title: string;
  content: string;
}

const ANIMATION_DURATION = 350;

const ListItem = ({ item }: { item: AccordianContent }) => {
  const [isExpanded, setIsExpanded] = useState<boolean>(false);
  const [isOpen, setIsOpen] = useState<boolean>(false);

  const handleCollapsedExpand = () => {
    if (!isExpanded) {
      setIsOpen(true);
      setTimeout(() => setIsExpanded(true), 10);
    } else {
      setIsExpanded(false);
      setTimeout(() => setIsOpen(false), ANIMATION_DURATION);
    }
  };

  return (
    <>
      <button
        id={`header-${item.id}`}
        aria-controls={`content-${item.id}`}
        className={`accordian ${isExpanded ? "expanded" : ""}`}
        onClick={handleCollapsedExpand}
        aria-expanded={isExpanded}
      >
        <div className="accordian-title">{item.title}</div>
        <ChevronDown className={`accordian-arrow ${isExpanded ? "rotated" : ""}`} />
      </button>
      {isOpen && (
        <div role="region" aria-labelledby={`header-${item.id}`} id={`content-${item.id}`} className={`accordian-content-wrapper ${isExpanded ? "expanded" : ""}`}>
          <div className="accordian-content">{item.content}</div>
        </div>
      )}
    </>
  );
};

export const Accordian: React.FC<{ AccordianType: AccordianContent[] }> = (
  props,
) => {
  const { AccordianType } = props;

  return (
    <div className="accordian-container">
      {AccordianType.map((item) => (
        <ListItem key={item.id} item={item} />
      ))}
    </div>
  );
};
