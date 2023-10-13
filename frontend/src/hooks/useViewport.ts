import { useLayoutEffect, useState } from 'react';

function useViewport() {
  const [isMobile, setIsMobile] = useState(false);
  const [isLoaded, setIsLoaded] = useState(false);

  const handleResize = () => {
    setIsMobile(window.innerWidth <= 768 || window.outerWidth <= 768);
  };

  useLayoutEffect(() => {
    handleResize();
    setIsLoaded(true);
    window.addEventListener('resize', handleResize);
    return () => window.removeEventListener('resize', handleResize);
  }, []);

  return {
    isMobile,
    isLoaded,
  };
}

export default useViewport;
