import { useSelector } from 'react-redux';
import NextBtn from './button/NextBtn';
import TipTags from './Tips/TipTags';

export default function Tags({ isSelected, setisSelected }) {
  const tipboxNum = useSelector((state) => {
    return state.tipbox.value; // store 안에 reducer가 저장되어 있는 Slice의 이름
  });

  return (
    <div className="relative flex flex-col my-2 px-6 py-5 w-212 bg-white border-2 border-solid border-gray rounded-md">
      <div className="font-semibold">Tags</div>

      <div className="my-1 text-xs">
        Add up to 5 tags to describe what your question is about. Start typing
        to see suggestions.
      </div>

      <input
        className="mt-1 mb-3 py-1.5 pl-2 text-sm bg-white border-2 border-solid border-gray rounded-md"
        placeholder="e.g. (mysql json typescript)"
        onFocus={() => {
          setisSelected(4);
        }}
      ></input>

      {tipboxNum === 4 ? <NextBtn /> : null}
      {isSelected === 4 && <TipTags />}
    </div>
  );
}
