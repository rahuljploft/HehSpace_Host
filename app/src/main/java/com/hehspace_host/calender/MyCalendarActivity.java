package com.hehspace_host.calender;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hehspace_host.MainActivity;
import com.hehspace_host.R;
import com.hehspace_host.components.fragments.dashboard.home.HomeFragment;
import com.hehspace_host.components.property.PropertDetailsActivity;
import com.hehspace_host.model.DateListModel;
import com.hehspace_host.model.DateModel;
import com.hehspace_host.network.ApiResponse;
import com.hehspace_host.util.ProgressDialog;
import com.hehspace_host.util.Uitility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@TargetApi(3)
public class MyCalendarActivity extends BottomSheetDialogFragment implements OnClickListener {
	private static final String tag = "MyCalendarActivity";
	public GregorianCalendar cal_month;
	private TextView currentMonth1;
	private Button selectedDayMonthYearButton;
	private ImageView prevMonth;
	private ImageView nextMonth;
	private GridView calendarView;
	private GridCellAdapter adapter;
	private Calendar _calendar;
	int selecteddate = -1;
	@SuppressLint("NewApi")
	private int month, year;
	public static final String TAG = "MyCalendarActivity";
	List<DateModel> dateModelList = new ArrayList<>();
	MyCalenderView_Model model;
	private ItemClickListener mListener;
	public  static  String finaldate = "";

	@SuppressWarnings("unused")
	@SuppressLint({"NewApi", "NewApi", "NewApi", "NewApi"})
	private final DateFormat dateFormatter = new DateFormat();
	private static final String dateTemplate = "MMMM yyyy";

	public static MyCalendarActivity newInstance() {
		return new MyCalendarActivity();
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
							 @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_calendar_view, container, false);

		model = new MyCalenderView_Model();

		model.livedata.observe(this, modelApiResponse -> handleResult(modelApiResponse));

		if (Uitility.isOnline(requireContext())) {
			model.getDateList(Integer.parseInt(HomeFragment.propid));
		} else {
			Uitility.nointernetDialog(requireActivity());
		}

		_calendar = Calendar.getInstance(Locale.getDefault());
		month = _calendar.get(Calendar.MONTH) + 1;
		year = _calendar.get(Calendar.YEAR);
		Log.d(tag, "Calendar Instance:= " + "Month: " + month + " " + "Year: "
				+ year);

		selectedDayMonthYearButton = view.findViewById(R.id.selectedDayMonthYear);
		selectedDayMonthYearButton.setText("Selected: ");
		cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
		prevMonth = view.findViewById(R.id.prevMonth);
		prevMonth.setOnClickListener(this);

		currentMonth1 = view.findViewById(R.id.currentMonth);
		currentMonth1.setText(DateFormat.format(dateTemplate,
				_calendar.getTime()));

		nextMonth = view.findViewById(R.id.nextMonth);
		nextMonth.setOnClickListener(this);

		calendarView = view.findViewById(R.id.calendar);
		adapter = new GridCellAdapter(requireContext(),
				R.id.calendar_day_gridcell, month, year);
		calendarView.setAdapter(adapter);

		return view;
	}

	//	@Override
//	public void onAttach(Context context) {
//		super.onAttach(context);
//		if (context instanceof ItemClickListener) {
//			mListener = (ItemClickListener) context;
//		} else {
//			throw new RuntimeException(context.toString()
//					+ " must implement ItemClickListener");
//		}
//	}
//	@Override
//	public void onDetach() {
//		super.onDetach();
//		mListener = null;
//	}
	@SuppressLint("SimpleDateFormat")
	private void handleResult(ApiResponse<DateListModel> result) {
		switch (result.getStatus()) {
			case ERROR:
				ProgressDialog.hideProgressDialog();
				Log.e("qwerty", "error - " + result.getError().getMessage());
				break;
			case LOADING:
				ProgressDialog.showProgressDialog(requireActivity());
				break;
			case SUCCESS:
				ProgressDialog.hideProgressDialog();
				if (result.getData().responsecode.equals("200")) {
					if (result.getData().status.equals("true")) {

						if (result.getData().data != null) {
							for (int i = 0; i < result.getData().data.size(); i++) {
								String date = result.getData().data.get(i).datepass;
								try {
									@SuppressLint("SimpleDateFormat") SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
									Date newDate = null;
									newDate = spf.parse(date);
									spf = new SimpleDateFormat("dd-MMMM-yyyy");
									String newDateString = spf.format(newDate);
									Log.d("dateee", newDateString);
									DateModel dateModel = new DateModel();
									dateModel.setId(i + "");
									dateModel.setDate(newDateString);
									dateModel.setStatus(result.getData().data.get(i).status);
									dateModelList.add(dateModel);
								} catch (ParseException e) {
									e.printStackTrace();
								}
							}

							adapter.notifyDataSetChanged();
						}


					} else {
						adapter.notifyDataSetChanged();
						Toast.makeText(requireActivity(), result.getData().message, Toast.LENGTH_SHORT).show();
					}
				}
				break;
		}
	}


	/**
	 * @param month
	 * @param year
	 */
	private void setGridCellAdapterToDate(int month, int year) {
		adapter = new GridCellAdapter(requireContext(),
				R.id.calendar_day_gridcell, month, year);
		_calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
		currentMonth1.setText(DateFormat.format(dateTemplate,
				_calendar.getTime()));
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		if (v == prevMonth) {
			if (cal_month.get(GregorianCalendar.MONTH) == (month - 1) && cal_month.get(GregorianCalendar.YEAR) == year) {
				//cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1), cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
				Toast.makeText(requireContext(), "Event Detail is available for current session only.", Toast.LENGTH_SHORT).show();
			} else {
				if (month <= 1) {
					month = 12;
					year--;
				} else {
					month--;
				}
				Log.d("qwertyuiop", "Month: "
						+ month + " Year: " + year);
				setGridCellAdapterToDate(month, year);
			}


		}
		if (v == nextMonth) {
			if (month > 11) {
				month = 1;
				year++;
			} else {
				month++;
			}
			Log.d(tag, "Setting Next Month in GridCellAdapter: " + "Month: "
					+ month + " Year: " + year);
			setGridCellAdapterToDate(month, year);
		}

	}

	@Override
	public void onDestroy() {
		Log.d(tag, "Destroying View ...");
		super.onDestroy();
	}

	// Inner Class
	public class GridCellAdapter extends BaseAdapter implements OnClickListener {
		private static final String tag = "GridCellAdapter";
		private final Context _context;

		private final List<String> list;
		private static final int DAY_OFFSET = 1;
		private final String[] weekdays = new String[]{"Sun", "Mon", "Tue",
				"Wed", "Thu", "Fri", "Sat"};
		private final String[] months = {"January", "February", "March",
				"April", "May", "June", "July", "August", "September",
				"October", "November", "December"};
		private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30,
				31, 30, 31};
		private int daysInMonth;
		private int currentDayOfMonth;
		private int currentWeekDay;
		private TextView gridcell;
		private RelativeLayout rlmain;
		private TextView num_events_per_day;
		private final HashMap<String, Integer> eventsPerMonthMap;
		private final SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"dd-MMM-yyyy");

		// Days in Current Month
		public GridCellAdapter(Context context, int textViewResourceId,
							   int month, int year) {
			super();
			this._context = context;
			this.list = new ArrayList<String>();
			Log.d(tag, "==> Passed in Date FOR Month: " + month + " "
					+ "Year: " + year);
			Calendar calendar = Calendar.getInstance();
			setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
			setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
			Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
			Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
			Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());

			// Print Month
			printMonth(month, year);

			// Find Number of Events
			eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
		}

		private String getMonthAsString(int i) {
			return months[i];
		}

		private String getWeekDayAsString(int i) {
			return weekdays[i];
		}

		private int getNumberOfDaysOfMonth(int i) {
			return daysOfMonth[i];
		}

		public String getItem(int position) {
			return list.get(position);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		/**
		 * Prints Month
		 *
		 * @param mm
		 * @param yy
		 */
		private void printMonth(int mm, int yy) {
			Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
			int trailingSpaces = 0;
			int daysInPrevMonth = 0;
			int prevMonth = 0;
			int prevYear = 0;
			int nextMonth = 0;
			int nextYear = 0;

			int currentMonth = mm - 1;
			String currentMonthName = getMonthAsString(currentMonth);
			daysInMonth = getNumberOfDaysOfMonth(currentMonth);

			Log.d(tag, "Current Month: " + " " + currentMonthName + " having "
					+ daysInMonth + " days.");

			GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
			Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

			if (currentMonth == 11) {
				prevMonth = currentMonth - 1;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 0;
				prevYear = yy;
				nextYear = yy + 1;
				Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:"
						+ prevMonth + " NextMonth: " + nextMonth
						+ " NextYear: " + nextYear);
			} else if (currentMonth == 0) {
				prevMonth = 11;
				prevYear = yy - 1;
				nextYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 1;
				Log.d(tag, "**--> PrevYear: " + prevYear + " PrevMonth:"
						+ prevMonth + " NextMonth: " + nextMonth
						+ " NextYear: " + nextYear);
			} else {
				prevMonth = currentMonth - 1;
				nextMonth = currentMonth + 1;
				nextYear = yy;
				prevYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				Log.d(tag, "***---> PrevYear: " + prevYear + " PrevMonth:"
						+ prevMonth + " NextMonth: " + nextMonth
						+ " NextYear: " + nextYear);
			}

			int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
			trailingSpaces = currentWeekDay;

			Log.d(tag, "Week Day:" + currentWeekDay + " is "
					+ getWeekDayAsString(currentWeekDay));
			Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
			Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

			if (cal.isLeapYear(cal.get(Calendar.YEAR)))
				if (mm == 2)
					++daysInMonth;
				else if (mm == 3)
					++daysInPrevMonth;

			// Trailing Month days
			for (int i = 0; i < trailingSpaces; i++) {
				Log.d(tag,
						"PREV MONTH:= "
								+ prevMonth
								+ " => "
								+ getMonthAsString(prevMonth)
								+ " "
								+ String.valueOf((daysInPrevMonth
								- trailingSpaces + DAY_OFFSET)
								+ i));
				list.add(String
						.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET)
								+ i)
						+ "-GREY"
						+ "-"
						+ getMonthAsString(prevMonth)
						+ "-"
						+ prevYear);
			}

			SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
			Date date = new Date();
			int k  = Integer.parseInt(dateFormat.format(date));

			// Current Month Days
			// Current Month Days
			for (int i = 1; i <= daysInMonth; i++) {
				Log.d(currentMonthName, String.valueOf(i) + " "
						+ getMonthAsString(currentMonth) + " " + yy);
				if(k == (currentMonth+1)){
					if (i == getCurrentDayOfMonth()) {
						list.add(String.valueOf(i) + "-BLUE" + "-"
								+ getMonthAsString(currentMonth) + "-" + yy);
					} else if (i < getCurrentDayOfMonth()) {
						list.add(String.valueOf(i) + "-YELLOW" + "-"
								+ getMonthAsString(currentMonth) + "-" + yy);
					} else {
						list.add(String.valueOf(i) + "-WHITE" + "-"
								+ getMonthAsString(currentMonth) + "-" + yy);
					}
				}else{
					Log.d("Month",dateFormat.format(date));
					Log.d("Month",currentMonth+"_");
					if (i < getCurrentDayOfMonth()) {
						list.add(String.valueOf(i) + "-WHITE" + "-"
								+ getMonthAsString(currentMonth) + "-" + yy);
					} else {
						list.add(String.valueOf(i) + "-WHITE" + "-"
								+ getMonthAsString(currentMonth) + "-" + yy);
					}
				}

			}


			// Leading Month days
			for (int i = 0; i < list.size() % 7; i++) {
				Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
				list.add(String.valueOf(i + 1) + "-GREY" + "-"
						+ getMonthAsString(nextMonth) + "-" + nextYear);
			}
		}

		/**
		 * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
		 * ALL entries from a SQLite database for that month. Iterate over the
		 * List of All entries, and get the dateCreated, which is converted into
		 * day.
		 *
		 * @param year
		 * @param month
		 * @return
		 */

		private HashMap<String, Integer> findNumberOfEventsPerMonth(int year,
																	int month) {
			HashMap<String, Integer> map = new HashMap<String, Integer>();

			return map;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = (LayoutInflater) _context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.screen_gridcell, parent, false);
			}

			// Get a reference to the Day gridcell
			gridcell = (TextView) row.findViewById(R.id.calendar_day_gridcell);
			rlmain = (RelativeLayout) row.findViewById(R.id.rlmain);
			gridcell.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					String date_month_year = (String) view.getTag();
//					for (int i = 0; i < dateModelList.size(); i++) {
//						if (dateModelList.get(i).getDate().equals(date_month_year)) {
//							if (dateModelList.get(i).getStatus().equals("Full")) {
//								Toast.makeText(requireActivity(), "already full", Toast.LENGTH_SHORT).show();
//								return;
//							}
//						}
//						Log.d("vishaltest", selecteddate+ "==3423423====="+position);
//					}
					selecteddate = position;
					notifyDataSetChanged();
					try {
						@SuppressLint("SimpleDateFormat") SimpleDateFormat spf = new SimpleDateFormat("dd-MMMM-yyyy");
						Date newDate = null;
						newDate = spf.parse(date_month_year);
						spf = new SimpleDateFormat("yyyy-MM-dd");
						String newDateString = spf.format(newDate);
						Log.d("dateee++=====", newDateString);
						finaldate = newDateString;
						//mListener.onItemClick(newDateString);
						//	dismiss();

						startActivity(new Intent(_context, MainActivity.class).putExtra("value","booking")
								.putExtra("my","no")
						);;
						Toast.makeText(requireActivity(), date_month_year, Toast.LENGTH_SHORT).show();
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			});
			num_events_per_day = (TextView) row
					.findViewById(R.id.num_events_per_day);
			// ACCOUNT FOR SPACING

			Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
			String[] day_color = list.get(position).split("-");
			String theday = day_color[0];
			String themonth = day_color[2];
			String theyear = day_color[3];
			if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
				if (eventsPerMonthMap.containsKey(theday)) {

					Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
					num_events_per_day.setText(numEvents.toString());
				}
			}

			// Set the Day GridCell
			gridcell.setText(theday);
			gridcell.setTag(theday + "-" + themonth + "-" + theyear);
			Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-"
					+ theyear);

			if (day_color[1].equals("GREY")) {
				gridcell.setTextColor(getResources()
						.getColor(R.color.lightgray));
				gridcell.setEnabled(false);
			}
			if (day_color[1].equals("YELLOW")) {
				gridcell.setTextColor(getResources()
						.getColor(R.color.lightgray));
				gridcell.setEnabled(false);
			}
			if (day_color[1].equals("WHITE")) {
				gridcell.setTextColor(getResources().getColor(
						R.color.dark));
				gridcell.setEnabled(true);
			}
			if (day_color[1].equals("BLUE")) {
				gridcell.setTextColor(getResources().getColor(R.color.purple_500));
			}
			Log.d("vishaltest", position+ "======="+selecteddate);
			if (position==selecteddate) {
				rlmain.setBackground(getResources().getDrawable(R.drawable.selected_calender));
				gridcell.setTextColor(_context.getResources().getColor(R.color.white));

			}else{
				rlmain.setBackground(null);
			}

			Log.d("dateModelList", day_color[1] + "=======");
			Log.d("dateModelList", theday + "-" + themonth + "-" + theyear + "=======");
			for (int i = 0; i < dateModelList.size(); i++) {
				Log.d("2134567890", "Setting GridCell " + theday + "-" + themonth + "-"
						+ theyear + "______" + dateModelList.get(i).getDate());

				if (dateModelList.get(i).getDate().equals(gridcell.getTag())) {
					if (dateModelList.get(i).getStatus().equals("Full")) {
						//gridcell.setTextColor(getResources().getColor(R.color.white));
						rlmain.setBackground(getResources().getDrawable(R.drawable.booked_calender));
						num_events_per_day.setText("Booked");
					} else if (dateModelList.get(i).getStatus().equals("Partial")) {
						//gridcell.setTextColor(getResources().getColor(R.color.white));
						rlmain.setBackground(getResources().getDrawable(R.drawable.free_calender));
						num_events_per_day.setText(dateModelList.get(i).getStatus());
					}

				}
			}
			return row;
		}

		@Override
		public void onClick(View view) {


		/*	selectedDayMonthYearButton.setText("Selected: " + date_month_year);
			Log.e("Selected date", date_month_year);
			try {
				@SuppressLint("SimpleDateFormat") SimpleDateFormat spf = new SimpleDateFormat("dd-MMMM-yyyy");
				Date newDate = null;
				newDate = spf.parse(date_month_year);
				spf = new SimpleDateFormat("yyyy-MM-dd");
				String newDateString = spf.format(newDate);
				Log.d("dateee",newDateString);

			} catch (ParseException e) {
				e.printStackTrace();
			}*/
		}

		public int getCurrentDayOfMonth() {
			return currentDayOfMonth;
		}

		private void setCurrentDayOfMonth(int currentDayOfMonth) {
			this.currentDayOfMonth = currentDayOfMonth;
		}

		public void setCurrentWeekDay(int currentWeekDay) {
			this.currentWeekDay = currentWeekDay;
		}

		public int getCurrentWeekDay() {
			return currentWeekDay;
		}
	}

	public interface ItemClickListener {
		void onItemClick(String item);
	}

}