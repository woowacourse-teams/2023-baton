import { useLayoutEffect, useState } from 'react';

function useViewport() {
  const [isMobile, setIsMobile] = useState(false);
  const [isLoaded, setIsLoaded] = useState(false);

  useLayoutEffect(() => {
    const mediaQuery = window.matchMedia('(max-width: 768px)');

    const handleResize = () => {
      setIsMobile(mediaQuery.matches);
    };

    handleResize();
    setIsLoaded(true);

    mediaQuery.addEventListener('change', handleResize);
    return () => {
      mediaQuery.removeEventListener('change', handleResize);
    };
  }, []);

  return {
    isMobile,
    isLoaded,
  };
}

export default useViewport;
