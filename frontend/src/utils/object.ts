type Primitive = null | undefined | string | number | boolean | symbol | bigint;

interface ObjType {
  [key: string | number]: Primitive | Object | any[];
}

export function deepEqual(object1: ObjType, object2: ObjType): boolean {
  if (Array.isArray(object1) && Array.isArray(object2)) {
    if (object1.length !== object2.length) return false;
  }

  for (const key in object1) {
    if (typeof object1[key] === 'object') {
      if (typeof object2[key] === 'object') {
        return deepEqual(object1[key] as ObjType, object2[key] as ObjType);
      }

      return false;
    }

    if (object1[key] !== object2[key]) {
      return false;
    }
  }

  return true;
}
