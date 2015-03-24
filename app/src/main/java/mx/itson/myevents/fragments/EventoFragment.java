package mx.itson.myevents.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

import mx.itson.myevents.R;
import mx.itson.myevents.adapters.EventoAdapter;
import mx.itson.myevents.models.Evento;
import mx.itson.myevents.soap.HomeServiceClient;
import mx.itson.myevents.views.EventoActivity;
import mx.itson.myevents.views.Main;

public class EventoFragment extends Fragment implements AbsListView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private EventoAdapter mAdapter;
    private EventoLoaderTask mEventoLoaderTask;
    private TextView tvCargando;
    private List<Evento> items;

    // TODO: Rename and change types of parameters
    public static EventoFragment newInstance(int sectionNumber) {
        EventoFragment fragment = new EventoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // TODO: Change Adapter to display your content
        if (mAdapter != null) {
            mAdapter = new EventoAdapter(this.getActivity(), items);
        }

        if (mEventoLoaderTask == null)
            mEventoLoaderTask = new EventoLoaderTask();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_evento, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        //((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
        tvCargando =(TextView) view.findViewById(android.R.id.empty);
        // Set OnItemClickListener so we can be notified on item clicks
        //mListView.setOnItemClickListener(this);
        mEventoLoaderTask.execute((Void) null);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((Main) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent evento=new Intent(getActivity(), EventoActivity.class);
        evento.putExtra("Id",items.get(position).getId());
        startActivity(evento);
    }

    public class EventoLoaderTask extends AsyncTask<Void, Void, List<Evento>> implements HomeServiceClient.Listener {

        @Override
        protected List<Evento> doInBackground(Void... params) {
            HomeServiceClient homeServiceClient = new HomeServiceClient(this);
            return homeServiceClient.getEventos();
        }

        @Override
        protected void onPostExecute(final List<Evento> eventos) {
            items=eventos;
            mAdapter = new EventoAdapter(EventoFragment.this.getActivity(),items);
            ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
            mListView.setOnItemClickListener(EventoFragment.this);
            tvCargando.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onError(String message) {

        }
    }

}
